package io.github.chakyl.sunrisegardening.util;

import io.github.chakyl.sunrisegardening.dataattachments.DayData;
import io.github.chakyl.sunrisegardening.dataattachments.TilledBlockChunkData;
import io.github.chakyl.sunrisegardening.registry.DataMapRegistry;
import io.github.chakyl.sunrisegardening.registry.SunriseRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;

import static io.github.chakyl.sunrisegardening.blocks.TilledSoilBlock.DAY_TICKS;

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
    public static int getTotalGrowTime(BlockState cropBlockState, CropBlock cropBlock) {
        Integer growTime = cropBlockState.getBlockHolder().getData(DataMapRegistry.CROP_GROW_TIMES);
        return growTime == null ? cropBlock.getMaxAge() : growTime;
    }

    public static int getCalculatedAge(DayData dayData, int maxAge, int totalGrowTime) {
        return Math.min(maxAge, (int) ((dayData.totalDaysGrown * (long) maxAge) / totalGrowTime));
    }

    public static void syncDayData(DayData dayData, BlockState cropBlockState, CropBlock cropBlock) {
        int totalGrowTime = getTotalGrowTime(cropBlockState, cropBlock);
        int maxAge = cropBlock.getMaxAge();
        int currentAge = cropBlockState.getValue(CropBlock.AGE);
        if (currentAge != getCalculatedAge(dayData, maxAge, totalGrowTime)) {
            dayData.totalDaysGrown = (currentAge * totalGrowTime + maxAge - 1) / maxAge;
        }

    }
}
