package io.github.chakyl.sunrisegardening.util.compat;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.neoforged.neoforge.common.CommonHooks;

import java.util.function.Supplier;
// Utils for this pattern: Crop grows up to a certain age (3 for example) and then places a block above it that has stages.
public class DoubleTallCompat {
    // This is a stupid method that gets the total age of rice-like crops. The initial crop can either be the top or bottom part so it needs to be this complicated probably
    public static int getDoubleCropAge(Level level, BlockPos pos, BlockState state, boolean isBottom, IntegerProperty lowerAgeProp, IntegerProperty upperAgeProp) {
        int firstAge = isBottom ? state.getValue(lowerAgeProp) : state.getValue(upperAgeProp);
        if (isBottom && firstAge < 3) return firstAge;

        BlockState secondCrop = level.getBlockState(isBottom ? pos.above() : pos);
        if (secondCrop.hasProperty(upperAgeProp)) {
            return secondCrop.getValue(upperAgeProp) + firstAge;
        } else if (!isBottom && secondCrop.hasProperty(lowerAgeProp)) {
            return secondCrop.getValue(lowerAgeProp) + firstAge;
        }
        return firstAge;
    }

    public static void growDoubleCrop(ServerLevel level, BlockState upperState, int doubledAge,  IntegerProperty lowerAgeProp,IntegerProperty upperAgeProp, int maxHalfAge, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (!CommonHooks.canCropGrow(level, pos, state, true)) return;

        int targetLowerAge = Math.min(doubledAge > 0 ? doubledAge : state.getValue(lowerAgeProp), maxHalfAge);
        if (state.getValue(lowerAgeProp) != targetLowerAge) {
            level.setBlock(pos, state.getBlock().defaultBlockState().setValue(lowerAgeProp, targetLowerAge), 2);
            CommonHooks.fireCropGrowPost(level, pos, state);
        }
        if (doubledAge > maxHalfAge) {
            growUpperCrop(level, pos, doubledAge, maxHalfAge, upperState, upperAgeProp);
        }
    }

    private static void growUpperCrop(ServerLevel level, BlockPos pos, int doubledAge, int maxHalfAge, BlockState upperState, IntegerProperty upperAgeProp) {
        BlockPos upperPos = pos.above();
        Block upperBlock = upperState.getBlock();
        int targetUpperAge = doubledAge - maxHalfAge;


        if (upperState.hasProperty(upperAgeProp)) {
            int currentUpperAge = upperState.getValue(upperAgeProp);
            int finalUpperAge = Math.min(Math.max(currentUpperAge, targetUpperAge), maxHalfAge);

            if (currentUpperAge != finalUpperAge) {
                level.setBlock(upperPos, upperState.setValue(upperAgeProp, finalUpperAge), 2);
                CommonHooks.fireCropGrowPost(level, upperPos, upperState);
            }
        } else if (level.isEmptyBlock(upperPos) && upperBlock.defaultBlockState().canSurvive(level, upperPos)) {
            int initialUpperAge = Math.min(targetUpperAge, maxHalfAge);
            BlockState newState = upperBlock.defaultBlockState().setValue(upperAgeProp, initialUpperAge);
            level.setBlockAndUpdate(upperPos, newState);
            CommonHooks.fireCropGrowPost(level, upperPos, newState);
        }
    }
}
