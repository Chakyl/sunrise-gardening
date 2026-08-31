package io.github.chakyl.sunrisegardening.util;

import io.github.chakyl.sunrisegardening.registry.DataMapRegistry;
import net.minecraft.world.item.ItemStack;
import org.violetmoon.quark.content.tweaks.module.HoeHarvestingModule;

public class HoeHarvestUtil {
    // Used by ${HoeHarvestingModuleMixin} for hoe aoe radius
    public static int getHoeRadius(ItemStack hoe) {
        if (HoeHarvestingModule.isHoe(hoe)) {
            Integer range = hoe.getItemHolder().getData(DataMapRegistry.HOE_AOE_RADIUS);
            return range == null ? 1 : range;
        }
        return 1;
    }
}
