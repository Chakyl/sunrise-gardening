package io.github.chakyl.sunrisegardening.blocks;

import cool.bot.botslib.util.Util;
import io.github.chakyl.sunrisegardening.SunriseGardening;
import io.github.chakyl.sunrisegardening.dataattachments.DayData;
import io.github.chakyl.sunrisegardening.registry.SunriseRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.material.Fluids;

import static io.github.chakyl.sunrisegardening.util.DayDataUtils.*;

public class TilledSoilBlock extends FarmBlock {
    // TODO: config value
    public static final int DAY_TICKS = 24000;

    public TilledSoilBlock(Properties properties) {
        super(properties);
    }
//    @Nullable
//    @Override
//    public BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction toolAction, boolean simulate) {
//        ItemStack itemStack = context.getItemInHand();
//
//        if (!itemStack.canPerformAction(toolAction)) {
//            return null;
//        }
//
//        if (ToolActions.SHOVEL_FLATTEN == toolAction && Config.shovelReverting) {
//            return Blocks.DIRT.defaultBlockState();
//        }
//
//        return super.getToolModifiedState(state, context, toolAction, simulate);
//    }

//    @Override
//    public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction facing, net.minecraftforge.common.IPlantable plantable) {
//        net.minecraftforge.common.PlantType plantType = plantable.getPlantType(world, pos.relative(facing));
//        return plantType == PlantType.CROP || plantType == PlantType.PLAINS;
//    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        LevelChunk chunk = level.getChunkAt(pos);

        if (level.isRainingAt(pos.above()) && SunriseGardening.CONFIG.rainWatering.get()) Util.setMoist(level, pos);
        else if (level.getFluidState(pos.above()).getType() == Fluids.WATER) Util.setMoist(level, pos);

        long currentDay = getCurrentDay(level);
        DayData dayData = getDayData(level, pos);
        long daysPassed = currentDay - dayData.dayLastGrown;

        if (daysPassed > 0) {
            processDailyLogic(level, pos, state, daysPassed, dayData);
            dayData.dayLastGrown = currentDay;

            chunk.setUnsaved(true);
        }

        scheduleNextTick(level, pos);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if (!level.isClientSide && !oldState.is(state.getBlock())) {
            LevelChunk chunk = level.getChunkAt(pos);
            chunk.getData(SunriseRegistry.AttachmentRegistry.TILLED_BLOCK_CHUNK_DATA).dayDataMap.put(pos, new DayData(getCurrentDay(level), 0));
            chunk.setUnsaved(true);
            scheduleNextTick(level, pos);
        }
        super.onPlace(state, level, pos, oldState, movedByPiston);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!level.isClientSide && !state.is(newState.getBlock())) {
            LevelChunk chunk = level.getChunkAt(pos);
            chunk.getData(SunriseRegistry.AttachmentRegistry.TILLED_BLOCK_CHUNK_DATA).dayDataMap.remove(pos);
            chunk.setUnsaved(true);
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    private void scheduleNextTick(Level level, BlockPos blockPos) {
        level.scheduleTick(blockPos, this, 10);
    }

    public static void processDailyLogic(ServerLevel level, BlockPos pos, BlockState state, long daysPassed, DayData dayData) {
        BlockPos cropPos = pos.above();
        BlockState cropBlockState = level.getBlockState(pos.above());

        if (!(cropBlockState.getBlock() instanceof CropBlock cropBlock)) return;
        if (cropBlock.isMaxAge(cropBlockState)) return;

        syncDayData(dayData, cropBlockState, cropBlock);

        dayData.totalDaysGrown += (int) daysPassed;

        level.setBlock(cropPos, cropBlock.getStateForAge(getCalculatedAge(dayData, cropBlock.getMaxAge(), getTotalGrowTime(cropBlockState, cropBlock))), 2);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        return true;
    }
}