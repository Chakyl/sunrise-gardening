package io.github.chakyl.sunrisegardening.util.compat;

import com.baisylia.culturaldelights.block.custom.CornBlock;
import com.baisylia.culturaldelights.block.custom.CornUpperBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.crop.RiceCropBlock;
import com.rosemods.windswept.common.block.WildBerryBushBlock;
import io.github.chakyl.sunrisegardening.SunriseGardening;
import io.github.chakyl.sunrisegardening.dataattachments.DayData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.CommonHooks;
import net.ribs.vintagedelight.block.GearoBerryBushBlock;
import net.satisfy.vinery.core.block.GrapeBush;
import vectorwing.farmersdelight.common.block.HangingTomatoBlock;
import vectorwing.farmersdelight.common.block.RiceBlock;
import vectorwing.farmersdelight.common.block.RicePaniclesBlock;
import vectorwing.farmersdelight.common.block.TomatoBlock;
import vectorwing.farmersdelight.common.registry.ModBlocks;

import static io.github.chakyl.sunrisegardening.util.DayDataUtils.getCalculatedAge;
import static vectorwing.farmersdelight.common.block.TomatoBlock.ROPELOGGED;

public class ModGrowthCompat {
    public static int getFDRiceAge(Level level, BlockPos pos, BlockState state, boolean isBottom) {
        return DoubleTallCompat.getDoubleCropAge(level, pos, state, isBottom, RiceBlock.AGE, RicePaniclesBlock.RICE_AGE);
    }

    public static void growFarmersDelightRice(ServerLevel level, int doubledAge,BlockPos pos) {
        DoubleTallCompat.growDoubleCrop(level, ModBlocks.RICE_CROP_PANICLES.get().defaultBlockState(), doubledAge,  RiceBlock.AGE, RicePaniclesBlock.RICE_AGE, 3, pos);
    }

    public static int getCDCornAge(Level level, BlockPos pos, BlockState state, boolean isBottom) {
        return DoubleTallCompat.getDoubleCropAge(level, pos, state, isBottom, CornBlock.AGE, CornUpperBlock.CORN_AGE);
    }

    public static void growCulturalDelightCorn(ServerLevel level, int doubledAge,  BlockPos pos) {
        DoubleTallCompat.growDoubleCrop(level, com.baisylia.culturaldelights.block.ModBlocks.CORN_UPPER.get().defaultBlockState(), doubledAge, CornBlock.AGE, CornUpperBlock.CORN_AGE, 3, pos);
    }

    public static void growKaleidoscopeRice(ServerLevel level, int totalGrowTime, Block pRiceBlock, BlockPos pos) {
        // TODO: Make it work with multi-day grow times
        RiceCropBlock riceBlock = (RiceCropBlock) pRiceBlock;
        BlockState state = level.getBlockState(pos);
        int age = state.getValue(RiceCropBlock.AGE);
        if (age <= riceBlock.getMaxAge()) {
            if (CommonHooks.canCropGrow(level, pos, state, true)) {
                if (age != riceBlock.getMaxAge()) {
                    if (level.getBlockState(pos.above()).getBlock() instanceof RiceCropBlock ricePaniclesBlock) {
                        if (totalGrowTime < ricePaniclesBlock.getMaxAge()) {
                            int flags = 2 | 16;
                            level.setBlock(pos, state.setValue(RiceCropBlock.AGE, totalGrowTime), flags);
                            level.setBlock(pos.above(), state.setValue(RiceCropBlock.AGE, totalGrowTime).setValue(RiceCropBlock.WATERLOGGED, false).setValue(RiceCropBlock.LOCATION, 1), flags);
                        }
                    }
                }
            }
        }

    }

    public static boolean isVineLike(Block questionableVineLike) {
        if (questionableVineLike instanceof TomatoBlock tomatoBlock) {
            return true;
        }
//        if (SunriseGardening.HEARTH_AND_HARVEST_INSTALLED && questionableVineLike instanceof RedGrapeVineBlock) {
//            return true;
//        } else
//            return SunriseGardening.HEARTH_AND_HARVEST_INSTALLED && questionableVineLike instanceof GreenGrapeVineBlock;
        return false;
    }

    public static void attemptVineLikeRopeClimb(ServerLevel level, Block vineBlock, BlockPos pos, RandomSource random) {
        if (vineBlock instanceof TomatoBlock tomatoBlock) {
            tomatoBlock.climbRopeAbove(level, pos);
        }
//        else if (SunriseGardening.HEARTH_AND_HARVEST_INSTALLED && vineBlock instanceof RedGrapeVineBlock) {
//            ((RedGrapeVineBlock) vineBlock).attemptRopeClimb(level, pos, random);
//        } else if (SunriseGardening.HEARTH_AND_HARVEST_INSTALLED && vineBlock instanceof GreenGrapeVineBlock) {
//            ((GreenGrapeVineBlock) vineBlock).attemptRopeClimb(level, pos, random);
//        }
        else {
            SunriseGardening.LOGGER.warn("Vinelike rope climb attempted on a weird block! Tell Chakyl!");
        }
    }

    public static void growVineLikeCrop(ServerLevel level, Block vineBlock, BlockState crop, BlockPos pos, int calculatedAge) {
        RandomSource random = level.getRandom();
        BlockPos aboveCropPos = pos.offset(0, 1, 0);
        BlockState vine = level.getBlockState(aboveCropPos);
        BlockState newState;
        if (vine.getBlock() instanceof HangingTomatoBlock hangingTomatoBlock) {
            if (!hangingTomatoBlock.isMaxAge(vine)) {
                newState = hangingTomatoBlock.getStateForAge(calculatedAge);
                level.setBlock(aboveCropPos, newState, 2);
            }
            aboveCropPos = pos.offset(0, 2, 0);
            vine = level.getBlockState(aboveCropPos);
            if (vine.getBlock() instanceof HangingTomatoBlock aboveHangingTomato) {
                if (!aboveHangingTomato.isMaxAge(vine)) {
                    newState = aboveHangingTomato.getStateForAge(calculatedAge);
                    level.setBlock(aboveCropPos, newState, 2);
                }
            } else {
                attemptVineLikeRopeClimb(level, vineBlock, pos.offset(0, 1, 0), random);
            }
        } else if (vine.getBlock() instanceof CropBlock aboveVineBlock && isVineLike(aboveVineBlock)) {
            if (!aboveVineBlock.isMaxAge(vine)) {
                newState = aboveVineBlock.getStateForAge(calculatedAge).setValue(ROPELOGGED, true);
                level.setBlock(aboveCropPos, newState, 2);
            }
            aboveCropPos = pos.offset(0, 2, 0);
            vine = level.getBlockState(aboveCropPos);
            if (vine.getBlock() instanceof CropBlock aboveAboveVineBlock && isVineLike(aboveAboveVineBlock)) {
                if (!aboveVineBlock.isMaxAge(vine)) {
                    newState = aboveAboveVineBlock.getStateForAge(calculatedAge).setValue(ROPELOGGED, true);
                    level.setBlock(aboveCropPos, newState, 2);
                }
            } else {
                attemptVineLikeRopeClimb(level, vineBlock, pos.offset(0, 1, 0), random);
            }
        } else {
            attemptVineLikeRopeClimb(level, vineBlock, pos, random);
        }

    }

    public static void growGearoBerryBush(ServerLevel level, DayData dayData, int totalGrowTime, BlockState bushBlock, BlockPos pos) {
        int bushAge = level.getBlockState(pos).getValue(GearoBerryBushBlock.AGE);
        if (bushAge < GearoBerryBushBlock.MAX_AGE)
            level.setBlock(pos, bushBlock.getBlock().defaultBlockState().setValue(GearoBerryBushBlock.AGE, getCalculatedAge(dayData, bushBlock, totalGrowTime)), 2);
    }

    public static void growGrapeBush(ServerLevel level, DayData dayData, int totalGrowTime, BlockState bushBlock, BlockPos pos) {
        int bushAge = level.getBlockState(pos).getValue(GrapeBush.AGE);
        if (bushAge < 3)
            level.setBlock(pos, bushBlock.getBlock().defaultBlockState().setValue(GrapeBush.AGE, getCalculatedAge(dayData, bushBlock, totalGrowTime)), 2);
    }

    public static void growWildBerryBush(ServerLevel level, DayData dayData, int totalGrowTime, BlockState bushBlock, BlockPos pos) {
        int bushAge = level.getBlockState(pos).getValue(WildBerryBushBlock.AGE);
        if (bushAge < 3)
            level.setBlock(pos, bushBlock.getBlock().defaultBlockState().setValue(WildBerryBushBlock.AGE, getCalculatedAge(dayData, bushBlock, totalGrowTime)), 2);
    }

//    public static void growTallFlowerCropBlock(ServerLevel level, BlockState crop, Block cropBlock, BlockPos pos, BlockState farmland) {
//        if (crop.getValue(SEGMENT) == DoubleBlockHalf.UPPER) {
//            pos = pos.below();
//        }
//        int age = ((TallFlowerCropBlock) cropBlock).getAge(crop);
//        int growthAge = age + getFertilizerIncrease(age, 3, farmland);
//        growthAge = Math.min(growthAge, ((TallFlowerCropBlock) cropBlock).getMaxAge());
//        if (growthAge >= ((TallFlowerCropBlock) cropBlock).getUpperSegmentAge()) {
//            if (!((TallFlowerCropBlock) cropBlock).canGrowUp(level, pos)) {
//                return;
//            }
//            level.setBlock(pos.above(), ((TallFlowerCropBlock) cropBlock).getStateForAge(growthAge).setValue(SEGMENT, DoubleBlockHalf.UPPER), 2);
//        }
//
//        level.setBlock(pos, ((TallFlowerCropBlock) cropBlock).getStateForAge(growthAge), 2);
//    }
//
//    public static void growRevivalHerbBlock(ServerLevel level, Block revivalHerbBlock, BlockPos pos, BlockState farmland) {
//        BlockState herbState = level.getBlockState(pos);
//        int mintAge = ((CropBlock) revivalHerbBlock).getAge(herbState);
//        if (mintAge < 8) {
//            level.setBlock(pos, herbState.setValue(RevivalHerbBlock.Companion.getAGE(), mintAge + getFertilizerIncrease(mintAge, RevivalHerbBlock.MAX_AGE, farmland)), 2);
//        }
//    }
}