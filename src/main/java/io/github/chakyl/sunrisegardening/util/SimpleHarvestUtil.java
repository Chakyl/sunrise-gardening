package io.github.chakyl.sunrisegardening.util;

import io.github.chakyl.sunrisegardening.registry.DataMapRegistry;
import io.github.chakyl.sunrisegardening.registry.SunriseTags;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;

public class SimpleHarvestUtil {
    // Used by various mixins to handle single harvest/regrowable crops
    public static BlockState handleCropHarvest(Level level, BlockPos pos,  BlockState blockState) {
        if (blockState.is(SunriseTags.SINGLE_HARVEST_CROP)) {
            level.playSound(null, pos, SoundEvents.CROP_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
            return Blocks.AIR.defaultBlockState();
        }
        Integer age = blockState.getBlockHolder().getData(DataMapRegistry.MULTI_HARVEST_CROPS_DATA);
        if (age == null) return blockState;
        if (blockState.getBlock() instanceof CropBlock cropBlock) {
            // TODO: make multi harvest crop data use resolved age rather than blockstate age
            IntegerProperty ageProperty = null;

            for (Property<?> prop : blockState.getProperties()) {
                if (prop instanceof IntegerProperty intProp && prop.getName().equals("age")) {
                    ageProperty = intProp;
                    break;
                }
            }

            if (ageProperty != null) {
                return blockState.setValue(ageProperty, Math.min(age, cropBlock.getMaxAge()));
            }
        }
        return blockState;
    }
}
