package io.github.chakyl.sunrisegardening.mixin;

import io.github.chakyl.sunrisegardening.util.HoeHarvestUtil;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.content.tweaks.module.HoeHarvestingModule;

@Mixin(value = HoeHarvestingModule.class, remap = false)
public abstract class HoeHarvestingModuleMixin {

    @Inject(method = "getRange", at = @At("HEAD"), cancellable = true)
    private static void getHoeRange(ItemStack hoe, CallbackInfoReturnable<Integer> cir) {
        if (!Quark.ZETA.modules.isEnabled(HoeHarvestingModule.class)) {
            return;
        }
        int radius = HoeHarvestUtil.getHoeRadius(hoe);
        if (radius > 1) {
            cir.setReturnValue(radius);
        }
    }
}