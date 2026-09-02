package io.github.chakyl.sunrisegardening.jade;


import io.github.chakyl.sunrisegardening.dataattachments.DayData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import static io.github.chakyl.sunrisegardening.SunriseGardening.loc;
import static io.github.chakyl.sunrisegardening.util.DayDataUtils.getDayData;
import static io.github.chakyl.sunrisegardening.util.DayDataUtils.getTotalGrowTime;

public enum CropInfoProvider implements  IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    @Override
    public void appendTooltip(
            ITooltip tooltip,
            BlockAccessor accessor,
            IPluginConfig config
    ) {
        BlockState state = accessor.getBlockState();
        if (accessor.getServerData().contains("days_grown")) {
            if (state.getBlock() instanceof CropBlock cropBlock) {
                if (!cropBlock.isMaxAge(state)) {
                    tooltip.clear();
                    tooltip.add(accessor.getBlock().getName());
                    tooltip.add(Component.literal("Growth: " + accessor.getServerData().get("days_grown") + "/" + accessor.getServerData().get("total_grow_time")));
                }
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return loc("crop_info");
    }

    @Override
    public void appendServerData(CompoundTag data, BlockAccessor accessor) {
        ServerLevel level = (ServerLevel) accessor.getLevel();
        BlockPos pos = accessor.getPosition();
        final DayData dayData = getDayData(level, pos.below());
        data.putInt("days_grown", dayData.totalDaysGrown);
        data.putInt("total_grow_time", getTotalGrowTime(accessor.getBlockState(), (CropBlock) accessor.getBlock()));
    }


}