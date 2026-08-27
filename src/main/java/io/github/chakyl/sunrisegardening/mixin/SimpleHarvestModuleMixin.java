package io.github.chakyl.sunrisegardening.mixin;

import io.github.chakyl.sunrisegardening.util.SimpleHarvestUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(targets = "org.violetmoon.quark.content.tweaks.module.SimpleHarvestModule", remap = false)
public class SimpleHarvestModuleMixin {
    @ModifyVariable(method = "harvestAndReplant",at = @At("STORE"),name = "newBlock")
    private static BlockState modifyReplantedBlock(BlockState original) {
        return SimpleHarvestUtil.handleCropHarvest(original);
    }
}