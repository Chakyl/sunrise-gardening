package io.github.chakyl.sunrisegardening.util;

import io.github.chakyl.sunrisegardening.registry.DataMapRegistry;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;

public class SimpleHarvestUtil {
    public static BlockState setReplantedCropStage(BlockState blockState) {
        Integer age = blockState.getBlockHolder().getData(DataMapRegistry.MULTI_HARVEST_CROPS_DATA);
        if (age == null) return blockState;
        if (blockState.getBlock() instanceof CropBlock cropBlock) {
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
