package io.github.chakyl.sunrisegardening.jade;


import io.github.chakyl.sunrisegardening.SunriseGardening;
import io.github.chakyl.sunrisegardening.dataattachments.DayData;
import io.github.chakyl.sunrisegardening.registry.SunriseTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.theme.IThemeHelper;

import static io.github.chakyl.sunrisegardening.SunriseGardening.loc;
import static io.github.chakyl.sunrisegardening.util.CropHandlerUtils.getSoil;
import static io.github.chakyl.sunrisegardening.util.CropHandlerUtils.getSoilPos;
import static io.github.chakyl.sunrisegardening.util.DayDataUtils.*;
import static io.github.chakyl.sunrisegardening.util.SeasonUtils.canGrowInSeason;

public enum CropInfoProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;


    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig pluginConfig) {
        if (!accessor.getServerData().getBoolean("sunrise")) return;
        Block block = accessor.getBlock();
        BlockPos position = accessor.getPosition();
        Level level = accessor.getLevel();
        BlockState state = accessor.getBlockState();

        String blockId = BuiltInRegistries.BLOCK.getKey(block).toString();
        BlockState soil = getSoil(level, position);
        boolean boosted = soil != null && (soil.is(SunriseTags.WEAK_FERTILIZED_FARMLAND) || soil.is(SunriseTags.STRONG_FERTILIZED_FARMLAND) || soil.is(SunriseTags.HYPER_FERTILIZED_FARMLAND));

        boolean canGrow = accessor.getServerData().getBoolean("can_grow");
        if (level.getBlockState(position).is(SunriseTags.CANCEL_RANDOM_TICK)) {
            addGrowthLevelTooltip(tooltip, accessor, boosted, canGrow);
        } else if ("minecraft:torchflower".equals(blockId)) {
            tooltip.add(Component.translatable("tooltip.jade.crop_mature").withStyle(net.minecraft.ChatFormatting.GREEN));
        } else {
            if (!level.getBlockState(position).is(SunriseTags.CANCEL_RANDOM_TICK) && !canGrow) {
                tooltip.add(Component.translatable("jade.sunrisegardening.crop_growth.stop").withStyle(net.minecraft.ChatFormatting.RED));
            }
        }

        if (accessor.getServerData().getBoolean("needs_farmland") && soil == null) {
            if (blockId.contains("rice")) {
                tooltip.add(Component.translatable("jade.sunrisegardening.crop_growth.need_watered_farmland").withStyle(net.minecraft.ChatFormatting.RED));
            }  else if (state.is(SunriseTags.GROWS_ON_SAND)) {
                tooltip.add(Component.translatable("jade.sunrisegardening.crop_growth.need_tilled_sand").withStyle(net.minecraft.ChatFormatting.RED));
            }else {
                tooltip.add(Component.translatable("jade.sunrisegardening.crop_growth.need_farmland").withStyle(net.minecraft.ChatFormatting.RED));
            }
        }
    }

    private void addGrowthLevelTooltip(ITooltip tooltip, BlockAccessor accessor, boolean boosted, boolean canGrow) {
        int daysGrown = accessor.getServerData().getInt("days_grown");
        int growTime = accessor.getServerData().getInt("total_grow_time");
        MutableComponent ageText = Component.literal(String.valueOf(daysGrown));
        MutableComponent maxAgeText = Component.literal(String.valueOf(growTime));

        if (boosted) {
            ageText.withStyle(net.minecraft.ChatFormatting.DARK_GREEN);
            maxAgeText.withStyle(net.minecraft.ChatFormatting.DARK_GREEN);
        }

        if (daysGrown >= growTime) {
        } else {
            tooltip.replace(JadeIds.MC_CROP_PROGRESS, Component.translatable("jade.sunrisegardening.crop_info", IThemeHelper.get().info(daysGrown + "/" + growTime)));
        }

        if (!canGrow) {
            tooltip.add(Component.translatable("jade.sunrisegardening.crop_growth.stop").withStyle(net.minecraft.ChatFormatting.RED));
        }
    }

    @Override
    public void appendServerData(CompoundTag data, BlockAccessor accessor) {
        ServerLevel level = (ServerLevel) accessor.getLevel();
        Block block = accessor.getBlock();
        BlockPos pos = accessor.getPosition();
        BlockState state = accessor.getBlockState();

        BlockPos soil = getSoilPos(level, pos);
        if (state.is(SunriseTags.SUNRISE_CROP)) {
            data.putBoolean("sunrise", true);
            if (soil != null && hasDayData(level, soil)) {
                boolean canGrow = canGrowInSeason((ServerLevel) level, state, state, pos);
                if (SunriseGardening.CONFIG.needsSun.get() && !level.canSeeSky(pos)) canGrow = false;

                data.putBoolean("can_grow", canGrow);

                final DayData dayData = getDayData(level, soil);
                data.putInt("days_grown", dayData.totalDaysGrown);
                data.putInt("total_grow_time", getTotalGrowTime(accessor.getBlockState(), level.getBlockState(pos.below())));
            } else {
                data.putBoolean("needs_farmland", true);
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return loc("crop_info");
    }
}