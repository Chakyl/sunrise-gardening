package io.github.chakyl.sunrisegardening.util;

import io.github.chakyl.sunrisegardening.SunriseGardening;
import io.github.chakyl.sunrisegardening.dataattachments.DayData;
import io.github.chakyl.sunrisegardening.dataattachments.TilledBlockChunkData;
import io.github.chakyl.sunrisegardening.registry.DataMapRegistry;
import io.github.chakyl.sunrisegardening.registry.SunriseRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.chunk.LevelChunk;

import static io.github.chakyl.sunrisegardening.blocks.TilledSoilBlock.DAY_TICKS;
import static io.github.chakyl.sunrisegardening.util.CropHandlerUtils.getMaxAgeFromAnyCrop;

public class DayDataUtils {

    public static long getCurrentDay(Level level) {
        return level.getDayTime() / DAY_TICKS;
    }

    public static DayData getDayData(ServerLevel level, BlockPos pos) {
        LevelChunk chunk = level.getChunkAt(pos);
        TilledBlockChunkData chunkData = chunk.getData(SunriseRegistry.AttachmentRegistry.TILLED_BLOCK_CHUNK_DATA);
        return chunkData.dayDataMap.computeIfAbsent(pos, blockPos -> new DayData(DayDataUtils.getCurrentDay(level), 0));
    }
    // TODO: handle fertilizers here?
    public static int getTotalGrowTime(BlockState cropBlockState, Block cropBlock) {
        Integer growTime = cropBlockState.getBlockHolder().getData(DataMapRegistry.CROP_GROW_TIMES);
        return growTime == null ? getMaxAgeFromAnyCrop(cropBlockState, cropBlock) : growTime;
    }

    public static int getCalculatedAge(DayData dayData, int maxAge, int totalGrowTime) {
        return Math.max(0, Math.min(maxAge, (int) ((dayData.totalDaysGrown * (long) maxAge) / totalGrowTime)));
    }

    // Makes the chunk attachment data reflect the blockstate age of the crop
    public static void syncDayData(DayData dayData, BlockState cropBlockState, CropBlock cropBlock) {
        int totalGrowTime = getTotalGrowTime(cropBlockState, cropBlock);
        int maxAge = cropBlock.getMaxAge();
        int currentAge = cropBlock.getAge(cropBlockState);
        if (currentAge != getCalculatedAge(dayData, maxAge, totalGrowTime)) {
            dayData.totalDaysGrown = Math.max(0, (currentAge * totalGrowTime + maxAge - 1) / maxAge);
        }
        if (dayData.totalDaysGrown < 0) dayData.totalDaysGrown = 0;
    }

    public static boolean compareDay(long day, long checkedDay, int amount) {
        return day < checkedDay || day - checkedDay >= amount;
    }
}
