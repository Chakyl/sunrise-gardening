package io.github.chakyl.sunrisegardening.blocks;

import cool.bot.botslib.util.RNG;
import cool.bot.botslib.util.Util;
import io.github.chakyl.sunrisegardening.SunriseGardening;
import io.github.chakyl.sunrisegardening.dataattachments.DayData;
import io.github.chakyl.sunrisegardening.registry.SunriseRegistry;
import io.github.chakyl.sunrisegardening.registry.SunriseTags;
import io.github.chakyl.sunrisegardening.util.CropHandlerUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.material.Fluids;
import sereneseasons.init.ModConfig;
import sereneseasons.init.ModFertility;

import static io.github.chakyl.sunrisegardening.util.CropHandlerUtils.isNearSprinkler;
import static io.github.chakyl.sunrisegardening.util.DayDataUtils.*;
import static io.github.chakyl.sunrisegardening.util.SeasonUtils.canGrowInSeason;
import static io.github.chakyl.sunrisegardening.util.SeasonUtils.isGlassAboveBlock;

public class TilledSoilBlock extends FarmBlock {
    // TODO: config value
    public static final int DAY_TICKS = 24000;

    public TilledSoilBlock(Properties properties) {
        super(properties);
    }
//    @Override
//    public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction facing, net.minecraftforge.common.IPlantable plantable) {
//        net.minecraftforge.common.PlantType plantType = plantable.getPlantType(world, pos.relative(facing));
//        return plantType == PlantType.CROP || plantType == PlantType.PLAINS;
//    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        LevelChunk chunk = level.getChunkAt(pos);
        BlockPos abovePos = pos.above();

        if (level.isRainingAt(abovePos) && SunriseGardening.CONFIG.rainWatering.get()) Util.setMoist(level, pos);
        else if (level.getFluidState(abovePos).getType() == Fluids.WATER) Util.setMoist(level, pos);

        long currentDay = getCurrentDay(level);
        DayData dayData = getDayData(level, pos);
        long daysPassed = Math.max(0, currentDay - dayData.dayLastGrown);

        if (compareDay(currentDay, dayData.dayLastGrown, 1)) {
            boolean nearSprinkler = SunriseGardening.CONFIG.checkSprinklers.get() && isNearSprinkler(level, pos);
            BlockState crop = level.getBlockState(abovePos);
            boolean becameDirt = false;
            if (!nearSprinkler && !Util.isMoistWaterable(level, pos)) {
                if (crop.is(Blocks.AIR) && RNG.mc_ihundo(random, SunriseGardening.CONFIG.dailyCroplessDecayChance.get())) {
                    becameDirt = true;
                } else if (RNG.mc_ihundo(random, SunriseGardening.CONFIG.dailyCropDecayChance.get())) {
                    becameDirt = true;
                }
            } else {
                if (!(crop.getBlock() instanceof AirBlock)) {
                    processDailyLogic(level, pos, state, daysPassed, dayData);
                }
            }
            if (becameDirt) level.setBlock(pos, Blocks.DIRT.defaultBlockState(), 3);
            else handleFarmlandDrying(state, level, pos, random, nearSprinkler, crop);
            dayData.dayLastGrown = currentDay;

            chunk.setUnsaved(true);
        }


        scheduleNextTick(level, pos);
    }

    private static void handleFarmlandDrying(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, boolean hasSprinkler, BlockState crop) {
        boolean hydrated = hasSprinkler;
        if (hydrated && Util.isDryWaterable(level, pos)) {
            Util.setMoist(level, pos);
            return;
        }
        if (RNG.mc_ihundo(random, SunriseGardening.CONFIG.dailyDryChance.get())) {
            if (!hydrated && state.is(SunriseTags.HYDRATING_FARMLAND) && crop.getBlock() instanceof CropBlock cropBlock && cropBlock.getAge(crop) < Math.floor((float) getTotalGrowTime(crop, cropBlock, state) / 2)) {
                hydrated = true;
            }
            if (state.is(SunriseTags.DELUXE_HYDRATING_FARMLAND)) {
                hydrated = true;
            }
            if (!hydrated) {
                Util.setDry(level, pos);
            }
        }
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
        boolean canGrow = canGrowInSeason(level, state, cropBlockState, cropPos);
        if (!canGrow) return;
        if (!(cropBlockState.getBlock() instanceof CropBlock cropBlock)) return;
        if (cropBlock.isMaxAge(cropBlockState)) return;

        syncDayData(dayData, cropBlockState, cropBlock, state);
        int totalGrowTime = getTotalGrowTime(cropBlockState, cropBlock, state);
        dayData.totalDaysGrown = (int) Math.min(daysPassed + dayData.totalDaysGrown, totalGrowTime);
        CropHandlerUtils.growCrop(level, dayData, totalGrowTime, cropBlockState, cropPos, state, false);
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