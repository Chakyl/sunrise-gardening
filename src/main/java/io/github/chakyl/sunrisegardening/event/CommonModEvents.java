package io.github.chakyl.sunrisegardening.event;

import io.github.chakyl.sunrisegardening.SunriseGardening;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

import static io.github.chakyl.sunrisegardening.registry.DataMapRegistry.*;

@EventBusSubscriber(modid = SunriseGardening.MODID)
public class CommonModEvents {
    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> SunriseGardening.QUALITY_FOOD_INSTALLED = ModList.get().isLoaded("quality_food"));
        event.enqueueWork(() -> SunriseGardening.KUBEJS_INSTALLED = ModList.get().isLoaded("kubejs"));
        event.enqueueWork(() -> SunriseGardening.SERENE_SEASONS_INSTALLED = ModList.get().isLoaded("sereneseasons"));
        event.enqueueWork(() -> SunriseGardening.FARMERS_DELIGHT_INSTALLED = ModList.get().isLoaded("farmersdelight"));
        event.enqueueWork(() -> SunriseGardening.VINTAGEDELIGHT_INSTALLED = ModList.get().isLoaded("vintagedelight"));
        event.enqueueWork(() -> SunriseGardening.SUPPLEMENTARIES_INSTALLED = ModList.get().isLoaded("supplementaries"));
        event.enqueueWork(() -> SunriseGardening.HAUNTED_HARVEST_INSTALLED = ModList.get().isLoaded("hauntedharvest"));
        event.enqueueWork(() -> SunriseGardening.CULTURAL_DELIGHTS_INSTALLED = ModList.get().isLoaded("culturaldelights"));
        event.enqueueWork(() -> SunriseGardening.WINDSWEPT_INSTALLED = ModList.get().isLoaded("windswept"));
        event.enqueueWork(() -> SunriseGardening.VINERY_INSTALLED = ModList.get().isLoaded("vinery"));
        event.enqueueWork(() -> SunriseGardening.FLOWERARY_INSTALLED = ModList.get().isLoaded("flowerary"));
        event.enqueueWork(() -> SunriseGardening.COBBLEMON_INSTALLED = ModList.get().isLoaded("cobblemon"));
        event.enqueueWork(() -> SunriseGardening.HEARTH_AND_HARVEST_INSTALLED = ModList.get().isLoaded("hearthandharvest"));
        event.enqueueWork(() -> SunriseGardening.KALEIDOSCOPE_COOKERY_INSTALLED = ModList.get().isLoaded("kaleidoscope_cookery"));
    }

    @SubscribeEvent
    public static void registerDataMapTypes(RegisterDataMapTypesEvent event) {
        event.register(CROP_GROW_TIMES);
        event.register(GROWTH_FERTILIZER_RATE);
        event.register(MULTI_HARVEST_CROPS_DATA);
        event.register(SPRINKLER_SIZE);

        event.register(HOE_AOE_RADIUS);

        event.register(PERMANENT_SEASON_BIOMES);
    }
}