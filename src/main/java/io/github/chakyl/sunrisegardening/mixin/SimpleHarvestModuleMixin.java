package io.github.chakyl.sunrisegardening.mixin;

import io.github.chakyl.sunrisegardening.util.SimpleHarvestUtil;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.violetmoon.quark.content.tweaks.module.SimpleHarvestModule;

@Mixin(value = SimpleHarvestModule.class, remap = false)
public class SimpleHarvestModuleMixin {
    @ModifyVariable(method = "harvestAndReplant", at = @At("STORE"), name = "newBlock")
    private static BlockState modifyReplantedBlock(BlockState original) {
        return SimpleHarvestUtil.handleCropHarvest(original);
    }
}