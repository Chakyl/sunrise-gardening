package io.github.chakyl.sunrisegardening.event;

import io.github.chakyl.sunrisegardening.SunriseGardening;
import io.github.chakyl.sunrisegardening.registry.SunriseRegistry;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;


@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(value = Dist.CLIENT, modid = SunriseGardening.MODID)
public class ClientModEvents {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(SunriseRegistry.BlockRegistry.GREENHOUSE_GLASS.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(SunriseRegistry.BlockRegistry.GREENHOUSE_GLASS_STAIRS.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(SunriseRegistry.BlockRegistry.GREENHOUSE_GLASS_SLAB.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(SunriseRegistry.BlockRegistry.GREENHOUSE_GLASS_PANE.get(), RenderType.translucent());
        });
    }
}
