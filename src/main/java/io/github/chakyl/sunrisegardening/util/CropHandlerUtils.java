package io.github.chakyl.sunrisegardening.util;

import com.baisylia.culturaldelights.block.custom.CornBlock;
import com.baisylia.culturaldelights.block.custom.CornUpperBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.crop.RiceCropBlock;
import com.rosemods.windswept.common.block.WildBerryBushBlock;
import io.github.chakyl.sunrisegardening.SunriseGardening;
import io.github.chakyl.sunrisegardening.dataattachments.DayData;
import io.github.chakyl.sunrisegardening.registry.DataMapRegistry;
import io.github.chakyl.sunrisegardening.registry.SunriseTags;
import net.mehvahdjukaar.supplementaries.common.block.blocks.FlaxBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.block.CropGrowEvent;
import net.ribs.vintagedelight.block.GearoBerryBushBlock;
import net.satisfy.vinery.core.block.GrapeBush;
import vectorwing.farmersdelight.common.block.BuddingBushBlock;
import vectorwing.farmersdelight.common.block.RiceBlock;
import vectorwing.farmersdelight.common.block.RicePaniclesBlock;

import static io.github.chakyl.sunrisegardening.util.DayDataUtils.*;
import static io.github.chakyl.sunrisegardening.util.compat.ModGrowthCompat.*;
import static io.github.chakyl.sunrisegardening.util.SeasonUtils.canGrowInSeason;
import static net.minecraft.world.level.block.SweetBerryBushBlock.AGE;


public class CropHandlerUtils {
    public static int getAgeFromAnyCrop(Level level, BlockPos pos, BlockState state) {
        Block crop = state.getBlock();
        if (crop instanceof RicePaniclesBlock) return getFDRiceAge(level, pos, state, false);
        if (crop instanceof RiceBlock) return getFDRiceAge(level, pos, state, true);
        if (crop instanceof CornUpperBlock) return getCDCornAge(level, pos, state, false);
        if (crop instanceof CornBlock) return getCDCornAge(level, pos, state, true);
        if (crop instanceof CropBlock cropBlock) return cropBlock.getAge(state);
        if (crop instanceof StemBlock) return state.getValue(StemBlock.AGE);
        if (crop instanceof SweetBerryBushBlock) return state.getValue(SweetBerryBushBlock.AGE);
        if (crop instanceof WildBerryBushBlock) return state.getValue(WildBerryBushBlock.AGE);
        if (crop instanceof GearoBerryBushBlock) return state.getValue(GearoBerryBushBlock.AGE);
        if (crop instanceof RiceCropBlock) return state.getValue(RiceCropBlock.AGE);
        // Cactuslikes
        if (crop instanceof CactusBlock) return 1;
        if (crop instanceof SugarCaneBlock) return 1;

        SunriseGardening.LOGGER.warn("Missing actual age for: " + crop.getDescriptionId() + "! Ask Chakyl for compat!");
        return 1;
    }


    public static int getMaxAgeFromAnyCrop(BlockState state) {
        Block crop = state.getBlock();
        if (crop instanceof RicePaniclesBlock ricePaniclesBlock) return ricePaniclesBlock.getMaxAge() + 3;
        if (crop instanceof RiceBlock riceBlock) return riceBlock.getMaxAge() + 3;
        if (crop instanceof CornUpperBlock cornUpperBlock) return cornUpperBlock.getMaxAge() + 3;
        if (crop instanceof CornBlock cornBlock) return cornBlock.getMaxAge() + 3;
        if (crop instanceof CropBlock cropBlock) return cropBlock.getMaxAge();
        if (crop instanceof StemBlock) return StemBlock.MAX_AGE;
        if (crop instanceof CactusBlock) return 1;
        if (crop instanceof SugarCaneBlock) return 1;
        if (crop instanceof PitcherCropBlock) return PitcherCropBlock.MAX_AGE;
        if (crop instanceof SweetBerryBushBlock) return SweetBerryBushBlock.MAX_AGE;
        if (crop instanceof GearoBerryBushBlock) return GearoBerryBushBlock.MAX_AGE;
        if (crop instanceof WildBerryBushBlock) return 3;
        if (crop instanceof RiceCropBlock) return RiceCropBlock.MAX_AGE;
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

    public static void placeStemFruit(ServerLevel level, BlockPos pos, Block stemBlock, Block block) {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos fruitPos = pos.relative(direction);
            BlockState blockstate = level.getBlockState(fruitPos.below());
            if (level.isEmptyBlock(fruitPos) && (blockstate.canSustainPlant(level, fruitPos.below(), Direction.UP, block.defaultBlockState()).isTrue() || blockstate.is(SunriseTags.CROP_GROWING) || blockstate.is(BlockTags.DIRT))) {
                level.setBlockAndUpdate(fruitPos, block.defaultBlockState());
                BlockState newState = stemBlock.defaultBlockState();
                level.setBlock(pos, newState, 2);
                break;
            }
        }
    }

    public static void growSweetBerryBush(ServerLevel level, DayData dayData, int totalGrowTime, BlockState bushBlock, BlockPos pos) {
        int bushAge = level.getBlockState(pos).getValue(AGE);
        if (bushAge < SweetBerryBushBlock.MAX_AGE)
            level.setBlock(pos, bushBlock.getBlock().defaultBlockState().setValue(AGE, getCalculatedAge(dayData, bushBlock, totalGrowTime)), 2);
    }

    private static void handleCropBlock(ServerLevel level, DayData dayData, int totalGrowTime, BlockState crop, BlockPos cropPos, CropBlock cropBlock, Block aboveBlock) {

        if (SunriseGardening.SUPPLEMENTARIES_INSTALLED && cropBlock instanceof FlaxBlock flaxBlock) {
            flaxBlock.growCropBy(level, cropPos, crop, getCalculatedAge(dayData, cropBlock.getMaxAge(), totalGrowTime));
        }
        // TODO: Haunted Harvest
//        else if (SunriseGardening.HAUNTED_HARVEST_INSTALLED && cropBlock instanceof CornBaseBlock cornBaseBlock) {
//            cornBaseBlock.growCropBy(level, cropPos, crop, getCalculatedAge(dayData, cropBlock.getMaxAge(), totalGrowTime));
//        }
        else if (SunriseGardening.KALEIDOSCOPE_COOKERY_INSTALLED && aboveBlock instanceof RiceCropBlock) {
            growKaleidoscopeRice(level, getCalculatedAge(dayData, cropBlock.getMaxAge(), totalGrowTime), aboveBlock, cropPos);
        } else if (!cropBlock.isMaxAge(crop)) {
            level.setBlock(cropPos, cropBlock.getStateForAge(getCalculatedAge(dayData, cropBlock.getMaxAge(), totalGrowTime)), 2);
            if (SunriseGardening.FARMERS_DELIGHT_INSTALLED && isVineLike(cropBlock)) {
                growVineLikeCrop(level, cropBlock, crop, cropPos, getCalculatedAge(dayData, cropBlock.getMaxAge(), totalGrowTime));
            }
        }
    }

    public static void handleCropGrowth(ServerLevel level, DayData dayData, int totalGrowTime, BlockState crop, BlockPos cropPos, BlockState farmland) {
        Block aboveBlock = crop.getBlock();
        if (crop.is(SunriseTags.NOT_DAILY)) return;
        if (aboveBlock instanceof CropBlock cropBlock) {
            handleCropBlock(level, dayData, totalGrowTime, crop, cropPos, cropBlock, aboveBlock);
        } else if (aboveBlock instanceof SweetBerryBushBlock sweetBerryBushBlock) {
            growSweetBerryBush(level, dayData, totalGrowTime, crop, cropPos);
        } else if (SunriseGardening.WINDSWEPT_INSTALLED && aboveBlock instanceof WildBerryBushBlock) {
            growWildBerryBush(level, dayData, totalGrowTime, crop, cropPos);
        } else if (SunriseGardening.VINERY_INSTALLED && aboveBlock instanceof GrapeBush) {
            growGrapeBush(level, dayData, totalGrowTime, crop, cropPos);
        } else if (SunriseGardening.VINTAGEDELIGHT_INSTALLED && aboveBlock instanceof GearoBerryBushBlock) {
            growGearoBerryBush(level, dayData, totalGrowTime, crop, cropPos);
        } else if (SunriseGardening.CULTURAL_DELIGHTS_INSTALLED && aboveBlock instanceof CornBlock) {
            growCulturalDelightCorn(level, getCalculatedAge(dayData, crop, totalGrowTime), cropPos);
        } else if (SunriseGardening.FARMERS_DELIGHT_INSTALLED && aboveBlock instanceof RiceBlock) {
            growFarmersDelightRice(level, getCalculatedAge(dayData, crop, totalGrowTime), cropPos);
        } else if (aboveBlock instanceof StemBlock stemBlock) {
            int stemAge = crop.getValue(StemBlock.AGE);
            if (stemAge == 7) {
                if (SunriseGardening.CONFIG.replaceStem.get()) {
                    if (stemBlock == Blocks.MELON_STEM) {
                        level.setBlockAndUpdate(cropPos, Blocks.MELON.defaultBlockState());
                    } else if (stemBlock == Blocks.PUMPKIN_STEM) {
                        level.setBlockAndUpdate(cropPos, Blocks.PUMPKIN.defaultBlockState());
                    }
                } else {
                    if (stemBlock == Blocks.MELON_STEM) {
                        placeStemFruit(level, cropPos, Blocks.MELON_STEM, Blocks.MELON);
                    } else if (stemBlock == Blocks.PUMPKIN_STEM) {
                        placeStemFruit(level, cropPos, Blocks.PUMPKIN_STEM, Blocks.PUMPKIN);
                    }
                }
            } else {
                level.setBlock(cropPos, crop.setValue(StemBlock.AGE, getCalculatedAge(dayData, StemBlock.MAX_AGE, totalGrowTime)), 2);
            }
        } else if (aboveBlock instanceof PitcherCropBlock pitcherCropBlock) {
            for (int i = 0; i < getCalculatedAge(dayData, crop, totalGrowTime); i++) {
                pitcherCropBlock.performBonemeal(level, level.getRandom(), cropPos, level.getBlockState(cropPos));
            }
        } else if (SunriseGardening.FARMERS_DELIGHT_INSTALLED && aboveBlock instanceof BuddingBushBlock bushBlock) {
            bushBlock.growPastMaxAge(crop, level, cropPos, level.getRandom());
        }
    }

    public static void growCactusLike(ServerLevel level, Block block, BlockPos pos, BlockState farmland) {
        int growth = farmland.is(SunriseTags.HYPER_FERTILIZED_FARMLAND) ? 2 : 1;
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
        if (level.getFluidState(pPos.above()).getType() == Fluids.WATER) return true;
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
        if (SunriseGardening.CONFIG.needsSun.get() && !level.canSeeSky(cropPos) && !cropBlockState.is(SunriseTags.IGNORES_SUN))
            canGrow = false;
        if (!canGrow) return;
        if (!cropBlockState.is(SunriseTags.SUNRISE_CROP)) return;
        if (cropBlockState.getBlock() instanceof CropBlock cropBlock && cropBlock.isMaxAge(cropBlockState)) return;

        syncDayData(level, pos, cropBlockState);
        int totalGrowTime = getTotalGrowTime(cropBlockState, state);
        if (daysPassed > 1) {
            if (SunriseGardening.CONFIG.checkSprinklers.get() && !isNearSprinkler(level, pos)) daysPassed = 1;
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