package io.github.chakyl.sunrisegardening;

import com.mojang.logging.LogUtils;
import io.github.chakyl.sunrisegardening.registry.SunriseRegistry;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.slf4j.Logger;

@Mod(SunriseGardening.MODID)
public class SunriseGardening {
    public static final String MODID = "sunrisegardening";
    public static final Logger LOGGER = LogUtils.getLogger();
    private static final ModConfigSpec.Builder CONFIG_BUILDER = new ModConfigSpec.Builder();
    public static final SunriseConfig CONFIG = new SunriseConfig(CONFIG_BUILDER);
    public static boolean QUALITY_FOOD_INSTALLED = false;
    public static boolean KUBEJS_INSTALLED = false;
    public static boolean SERENE_SEASONS_INSTALLED = false;
    public static boolean FARMERS_DELIGHT_INSTALLED = false;
    public static boolean VINTAGEDELIGHT_INSTALLED = false;
    public static boolean SUPPLEMENTARIES_INSTALLED = false;
    public static boolean HAUNTED_HARVEST_INSTALLED = false;
    public static boolean CULTURAL_DELIGHTS_INSTALLED = false;
    public static boolean WINDSWEPT_INSTALLED = false;
    public static boolean VINERY_INSTALLED = false;
    public static boolean FLOWERARY_INSTALLED = false;
    public static boolean COBBLEMON_INSTALLED = false;
    public static boolean HEARTH_AND_HARVEST_INSTALLED = false;
    public static boolean KALEIDOSCOPE_COOKERY_INSTALLED = false;

    public SunriseGardening(ModContainer container) {
        IEventBus bus = container.getEventBus();
        bus.addListener(this::commonSetup);
        SunriseRegistry.register(bus);
        container.registerConfig(ModConfig.Type.COMMON, CONFIG_BUILDER.build());

    }
    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    public static ResourceLocation loc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

}