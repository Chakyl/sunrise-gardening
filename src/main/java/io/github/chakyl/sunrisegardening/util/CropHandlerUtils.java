package io.github.chakyl.sunrisegardening.util;

import io.github.chakyl.sunrisegardening.SunriseGardening;
import io.github.chakyl.sunrisegardening.dataattachments.DayData;
import io.github.chakyl.sunrisegardening.registry.DataMapRegistry;
import io.github.chakyl.sunrisegardening.registry.SunriseTags;
import net.mehvahdjukaar.hauntedharvest.blocks.CornBaseBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.FlaxBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.block.CropGrowEvent;

import static io.github.chakyl.sunrisegardening.util.DayDataUtils.*;
import static io.github.chakyl.sunrisegardening.util.SeasonUtils.canGrowInSeason;


public class CropHandlerUtils {

    public static int getMaxAgeFromAnyCrop(BlockState state) {
        Block crop = state.getBlock();
        if (crop instanceof CropBlock cropBlock) return cropBlock.getMaxAge();
        if (crop instanceof CactusBlock) return 1;
        if (crop instanceof SugarCaneBlock) return 1;
        if (crop instanceof NetherWartBlock || crop instanceof BonemealableBlock) {
            if (state.hasProperty(BlockStateProperties.AGE_2)) {
                return 2;
            } else if (state.hasProperty(BlockStateProperties.AGE_3)) {
                return 3;
            } else if (state.hasProperty(BlockStateProperties.AGE_4)) {
                return 4;
            } else if (state.hasProperty(BlockStateProperties.AGE_5)) {
                return 5;
            } else if (state.hasProperty(BlockStateProperties.AGE_7)) {
                return 7;
            } else if (state.hasProperty(BlockStateProperties.AGE_15)) {
                return 15;
            }
        }
        SunriseGardening.LOGGER.warn("Missing actual age for: " + crop.getDescriptionId() + "! Ask Chakyl for compat!");
        return 1;
    }

    public static int getFertilizerIncrease(int age, int maxAge, BlockState farmland) {
        int increase = 1;
        if (age == 0) {
            Float fertilizerIncrease = farmland.getBlockHolder().getData(DataMapRegistry.GROWTH_FERTILIZER_RATE);
            if (fertilizerIncrease != null) increase += fertilizerIncrease;
        }
        return Math.min(increase, maxAge);
    }


    public static void growSweetBerryBush(ServerLevel level, SweetBerryBushBlock bushBlock, BlockPos pos, BlockState farmland) {
        int bushAge = level.getBlockState(pos).getValue(SweetBerryBushBlock.AGE);
        if (bushAge < SweetBerryBushBlock.MAX_AGE)
            level.setBlock(pos, bushBlock.defaultBlockState().setValue(SweetBerryBushBlock.AGE, bushAge + getFertilizerIncrease(bushAge, SweetBerryBushBlock.MAX_AGE, farmland)), 2);
    }

    public static void handleCropGrowth(ServerLevel level, DayData dayData, int totalGrowTime, BlockState crop, BlockPos cropPos, BlockState farmland) {
        Block aboveBlock = crop.getBlock();
        if (crop.is(SunriseTags.NOT_DAILY)) return;
        if (aboveBlock instanceof CropBlock cropBlock) {
            handleCropBlock(level, dayData, totalGrowTime, crop, cropPos, farmland, cropBlock, aboveBlock);
        } else if (aboveBlock instanceof SweetBerryBushBlock sweetBerryBushBlock) {
            growSweetBerryBush(level, sweetBerryBushBlock, cropPos, farmland);
        }
//        else if (SunriseGardening.WINDSWEPT_INSTALLED && aboveBlock instanceof WildBerryBushBlock) {
//            growWildBerryBush(level, aboveBlock, cropPos, farmland);
//        } else if (SunriseGardening.VINERY_INSTALLED && aboveBlock instanceof GrapeBush) {
//            growGrapeBush(level, aboveBlock, cropPos, farmland);
//        } else if (SunriseGardening.VINTAGEDELIGHT_INSTALLED && aboveBlock instanceof GearoBerryBushBlock) {
//            growGearoBerryBush(level, aboveBlock, cropPos, farmland);
//        } else if (SunriseGardening.CULTURAL_DELIGHTS_INSTALLED && aboveBlock instanceof CornBlock) {
//            growCulturalDelightCorn(level, aboveBlock, cropPos, CornBlock.AGE, CornUpperBlock.AGE, farmland);
//        } else if (SunriseGardening.FARMERS_DELIGHT_INSTALLED && aboveBlock instanceof RiceBlock) {
//            growFarmersDelightRice(level, aboveBlock, cropPos, farmland);
//        }
//        else if (aboveBlock instanceof CustomStemBlock stemBlock) {
//            int stemAge = stemBlock.getAge(crop);
//            if (stemAge == 7) {
//                stemBlock.placeFruit(level, cropPos);
//            } else {
//                growthAmount = getFertilizerIncrease(stemAge, 7, farmland);
//                newState = stemBlock.getStateForAge(stemAge + growthAmount);
//                level.setBlock(cropPos, newState, 2);
//            }
//        } else if (aboveBlock instanceof PitcherCropBlock pitcherCropBlock) {
//            int pitcherAge = level.getBlockState(cropPos).getValue(PitcherCropBlock.AGE);
//            growthAmount = getFertilizerIncrease(pitcherAge, PitcherCropBlock.MAX_AGE, farmland);
//            for (int i = 0; i < growthAmount; i++) {
//                pitcherCropBlock.performBonemeal(level, random, cropPos, level.getBlockState(cropPos));
//            }
//        }
//        else if (SunriseGardening.FARMERS_DELIGHT_INSTALLED && aboveBlock instanceof BuddingBushBlock bushBlock) {
//            bushBlock.growPastMaxAge(crop, level, cropPos, level.getRandom());
//        }
    }

    private static void handleCropBlock(ServerLevel level, DayData dayData, int totalGrowTime, BlockState crop, BlockPos cropPos, BlockState farmland, CropBlock cropBlock, Block aboveBlock) {

        if (SunriseGardening.SUPPLEMENTARIES_INSTALLED && cropBlock instanceof FlaxBlock flaxBlock) {
            flaxBlock.growCropBy(level, cropPos, crop, getFertilizerIncrease(cropBlock.getAge(crop), cropBlock.getMaxAge(), farmland));
        } else if (SunriseGardening.HAUNTED_HARVEST_INSTALLED && cropBlock instanceof CornBaseBlock cornBaseBlock) {
            cornBaseBlock.growCropBy(level, cropPos, crop, getFertilizerIncrease(cropBlock.getAge(crop), cropBlock.getMaxAge(), farmland));
        }
//        else if (SunriseGardening.KALEIDOSCOPE_COOKERY_INSTALLED && aboveBlock instanceof RiceCropBlock) {
//            growKaleidoscopeRice(level, aboveBlock, cropPos);
//        }
        else if (!cropBlock.isMaxAge(crop)) {
            level.setBlock(cropPos, cropBlock.getStateForAge(getCalculatedAge(dayData, cropBlock.getMaxAge(), totalGrowTime)), 2);
//            if (SunriseGardening.FARMERS_DELIGHT_INSTALLED && isVineLike(cropBlock)) {
//                growVineLikeCrop(level, cropBlock, crop, cropPos, random);
//            }
        }
    }


    public static void growCactusLike(ServerLevel level, Block block, BlockPos pos, BlockState farmland) {
        int growth = farmland.is(SunriseTags.HYPER_FERTILIZED_FARMLAND)  ? 2 : 1;
        for (int i = 1; i < 3; ++i) {
            if (level.getBlockState(pos.above(i)).is(Blocks.AIR)) {
                BlockState defaultState = block.defaultBlockState();
                if (block instanceof SugarCaneBlock) {
                    level.setBlockAndUpdate(pos.above(i), defaultState);
                    CommonHooks.fireCropGrowPost(level, pos.above(i), defaultState);
                } else {
                    level.setBlockAndUpdate(pos.above(i), defaultState);
                    NeoForge.EVENT_BUS.post(new CropGrowEvent.Post(level, pos, defaultState, defaultState));
                    level.neighborChanged(pos.below(), block, pos);
                }
                if (growth > 1) {
                    growth--;
                } else break;
            }
        }
    }

    public static void handleSandCropGrowth(BlockState crop, ServerLevel level, BlockPos abovePos, BlockState farmland) {
        Block aboveBlock = crop.getBlock();
        if (aboveBlock instanceof CactusBlock cactusBlock) {
            growCactusLike(level, cactusBlock, abovePos, farmland);
        } else if (aboveBlock instanceof SugarCaneBlock sugarCaneBlock) {
            growCactusLike(level, sugarCaneBlock, abovePos, farmland);
        }
    }


    public static boolean inRange(Integer range, BlockPos pos, BlockPos center) {
        if (range == null || range < 1) return false;
        return Math.abs(pos.getX() - center.getX()) < range + 1 && Math.abs(pos.getZ() - center.getZ()) < range + 1;
    }


    public static boolean isNearSprinkler(LevelReader level, BlockPos pPos) {
        for (BlockPos blockpos : BlockPos.betweenClosed(pPos.offset(-4, 0, -4), pPos.offset(4, 1, 4))) {
            BlockState scannedState = level.getBlockState(blockpos);
            if (scannedState.is(SunriseTags.SPRINKLER) && inRange(scannedState.getBlockHolder().getData(DataMapRegistry.SPRINKLER_SIZE), blockpos, pPos))
                return true;
        }
        return false;
    }

//    public static void growCropsInRadius(ServerLevel level, BlockPos centerPos, int radius) {
//        for (BlockPos pos : BlockPos.betweenClosed(new BlockPos(centerPos.getX() - radius, centerPos.getY(), centerPos.getZ() - radius), new BlockPos(
//                centerPos.getX() + radius,
//                centerPos.getY(),
//                centerPos.getZ() + radius)
//        )) {
//            growCrop(level, level.getBlockState(pos), pos, level.getBlockState(centerPos.below()),false);
//        }
//    }

    public static void growCrop(ServerLevel level, DayData dayData, int totalGrowTime, BlockState crop, BlockPos cropPos, BlockState farmland, Boolean sandy) {

        if (sandy) {
            handleSandCropGrowth(crop, level, cropPos, farmland);
        } else {
            handleCropGrowth(level, dayData, totalGrowTime, crop, cropPos, farmland);
        }

    }
    public static void processDailyLogic(ServerLevel level, BlockPos pos, BlockState state, long daysPassed, DayData dayData) {
        processDailyLogic(level, pos, state, daysPassed, dayData, false);
    }

    public static void processDailyLogic(ServerLevel level, BlockPos pos, BlockState state, long daysPassed, DayData dayData, boolean sandy) {
        BlockPos cropPos = pos.above();
        BlockState cropBlockState = level.getBlockState(pos.above());
        boolean canGrow = canGrowInSeason(level, state, cropBlockState, cropPos);
        if (SunriseGardening.CONFIG.needsSun.get() && !level.canSeeSky(cropPos)) canGrow = false;
        if (!canGrow) return;
        if (!cropBlockState.is(SunriseTags.SUNRISE_CROP)) return;
        if (cropBlockState.getBlock() instanceof CropBlock cropBlock && cropBlock.isMaxAge(cropBlockState)) return;

        syncDayData(dayData, cropBlockState, state);
        int totalGrowTime = getTotalGrowTime(cropBlockState, state);
        if (totalGrowTime > 1 ) {
            if (SunriseGardening.CONFIG.checkSprinklers.get() && !isNearSprinkler(level, pos)) totalGrowTime = 1;
        }
        dayData.totalDaysGrown = (int) Math.min(daysPassed + dayData.totalDaysGrown, totalGrowTime);
        CropHandlerUtils.growCrop(level, dayData, totalGrowTime, cropBlockState, cropPos, state, sandy);
    }


    public static BlockState getSoil(Level level, BlockPos position) {
        for (int i = -4; i < 0; i++) {
            BlockPos scannedPos = position.above(i);
            BlockState scannedState = level.getBlockState(scannedPos);
            if (scannedState.is(SunriseTags.CROP_GROWING)) {
                return scannedState;
            }
        }
        return null;
    }

    public static BlockPos getSoilPos(Level level, BlockPos position) {
        for (int i = -4; i < 0; i++) {
            BlockPos scannedPos = position.above(i);
            BlockState scannedState = level.getBlockState(scannedPos);
            if (scannedState.is(SunriseTags.CROP_GROWING)) {
                return scannedPos;
            }
        }
        return null;
    }
}