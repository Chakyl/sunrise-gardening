package io.github.chakyl.sunrisegardening.event;

import io.github.chakyl.sunrisegardening.SunriseGardening;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

import static io.github.chakyl.sunrisegardening.registry.DataMapRegistry.MULTI_HARVEST_CROPS_DATA;

@EventBusSubscriber(modid = SunriseGardening.MODID)
public class CommonModEvents {
    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> SunriseGardening.QUALITY_FOOD_INSTALLED = ModList.get().isLoaded("quality_food"));
        event.enqueueWork(() -> SunriseGardening.KUBEJS_INSTALLED = ModList.get().isLoaded("kubejs"));
    }

    @SubscribeEvent
    public static void registerDataMapTypes(RegisterDataMapTypesEvent event) {
        event.register(MULTI_HARVEST_CROPS_DATA);
    }
}