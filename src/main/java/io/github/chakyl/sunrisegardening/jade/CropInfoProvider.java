package io.github.chakyl.sunrisegardening.jade;


import io.github.chakyl.sunrisegardening.SunriseGardening;
import io.github.chakyl.sunrisegardening.blocks.TilledSoilBlock;
import io.github.chakyl.sunrisegardening.dataattachments.DayData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.theme.IThemeHelper;

import static io.github.chakyl.sunrisegardening.SunriseGardening.loc;
import static io.github.chakyl.sunrisegardening.util.DayDataUtils.*;

public enum CropInfoProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;


    @Override
    public void appendTooltip(
            ITooltip tooltip,
            BlockAccessor accessor,
            IPluginConfig config
    ) {
        BlockState state = accessor.getBlockState();
        Block block = state.getBlock();
        if (accessor.getServerData().contains("days_grown") && accessor.getServerData().contains("total_grow_time")) {
            if (block instanceof BushBlock || block instanceof BonemealableBlock) {
                addMaturityTooltip(tooltip, accessor);
            }
        }
    }

    private static void addMaturityTooltip(ITooltip tooltip, BlockAccessor accessor) {
        MutableComponent component;
        int daysGrown = accessor.getServerData().getInt("days_grown");
        int growTime = accessor.getServerData().getInt("total_grow_time");
        if (daysGrown < growTime) {
            component = IThemeHelper.get().info(daysGrown + "/" + growTime);
        } else {
            component = IThemeHelper.get().success(Component.translatable("tooltip.jade.crop_mature"));
        }
        tooltip.replace(JadeIds.MC_CROP_PROGRESS, Component.translatable("tooltip.sunrisegardening.crop_info", component));
    }

    @Override
    public ResourceLocation getUid() {
        return loc("crop_info");
    }

    @Override
    public void appendServerData(CompoundTag data, BlockAccessor accessor) {
        ServerLevel level = (ServerLevel) accessor.getLevel();
        BlockPos pos = accessor.getPosition();
        if (hasDayData(level, pos.below()) && !(accessor.getBlock() instanceof TilledSoilBlock)) {
            final DayData dayData = getDayData(level, pos.below());
            data.putInt("days_grown", dayData.totalDaysGrown);
            data.putInt("total_grow_time", getTotalGrowTime(accessor.getBlockState(), accessor.getBlock(), level.getBlockState(pos.below())));
        }
    }


}