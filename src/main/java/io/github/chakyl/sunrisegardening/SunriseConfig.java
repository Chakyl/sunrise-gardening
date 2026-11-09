package io.github.chakyl.sunrisegardening;

import net.neoforged.neoforge.common.ModConfigSpec;

public class SunriseConfig {
    // Hydration
    public final ModConfigSpec.BooleanValue splashPotionWatering;
    public final ModConfigSpec.IntValue splashWaterArea;
    public final ModConfigSpec.BooleanValue bucketWatering;
    public final ModConfigSpec.BooleanValue bottleWatering;
    public final ModConfigSpec.BooleanValue rainWatering;
    public final ModConfigSpec.BooleanValue checkSprinklers;
    public final ModConfigSpec.BooleanValue waterSourceHydration;
    // Daily mechanics
    public final ModConfigSpec.BooleanValue dailyReset;
    public final ModConfigSpec.IntValue dailyTimeMin;
    public final ModConfigSpec.IntValue dailyDryChance;
    public final ModConfigSpec.IntValue dailyCropDecayChance;
    public final ModConfigSpec.IntValue dailyCroplessDecayChance;
    // Crop Growth
    public final ModConfigSpec.BooleanValue strictGreenhouses;
    public final ModConfigSpec.BooleanValue needsSun;
    public final ModConfigSpec.BooleanValue replaceStem;
    // Etc?
    public final ModConfigSpec.BooleanValue noTrample;
    public final ModConfigSpec.BooleanValue sturdyFarmland;
    public final ModConfigSpec.BooleanValue shovelReverting;

    public SunriseConfig(final ModConfigSpec.Builder builder) {
        builder.comment("");
        builder.comment("  ~~~ [ Farmland Hydration ] ~~~");
        builder.comment("");

        splashPotionWatering = builder.comment("Allows splash water bottles to water farmland").define("splash_potion_watering", true);

        splashWaterArea = builder.comment("The range of splash water bottles for watering, 0 to water only the block hit").defineInRange("customer_order_time", 1, 0, 8);

        bucketWatering = builder.comment("Allows water buckets to water farmland").define("bucket_watering", false);

        bottleWatering = builder.comment("Allows bottles to water farmland").define("bottle_watering", true);

        rainWatering = builder.comment("Allows rain to water farmland").define("rain_watering", true);

        checkSprinklers = builder.comment("Enables Crops checking for blocks with sprinkler tags to prevent drying").define("check_sprinklers", true);

        waterSourceHydration = builder.comment("Allows water source blocks to hydrate farmland. Best used if checkSprinklers is false ").define("water_source_hydration", false);

        builder.comment("");
        builder.comment("  ~~~ [ Daily Mechanics ] ~~~");
        builder.comment("");
        dailyReset = builder.comment("Causes farmland to dry daily").define("daily_reset", true);

        dailyTimeMin = builder.comment("The time of day daily reset logic will occur (within 10 ticks)").defineInRange("daily_time_min", 5, 0, 24000);

        dailyDryChance = builder.comment("The chance that farmland will become dry").defineInRange("daily_dry_chance", 100, 0, 100);

        dailyCropDecayChance = builder.comment("The chance that dry farmland will decay into dirt if it HAS a crop on top of it").defineInRange("daily_crop_decay_chance", 0, 0, 100);

        dailyCroplessDecayChance = builder.comment("The chance that dry farmland will decay into dirt if it DOES NOT HAVE a crop on top of it").defineInRange("daily_cropless_decay_chance", 25, 0, 100);

        builder.comment("");
        builder.comment("  ~~~ [ Crop Growth ] ~~~");
        builder.comment("");

        strictGreenhouses = builder.comment("Crops will not factor in greenhouse glass if farmland (#dewdrop:waterable) is between them and the glass").define("strict_greenhouses", true);

        needsSun = builder.comment("Crops need sky access to grow. If greenhouse glass is used, the glass needs sky access. Transparent blocks do not block sky access").define("needs_sun", true);

        replaceStem = builder.comment("Determines if stem crops like Pumpkins and Melons replace their stem when growing rather than trying to find a free spot").define("replace_stem", true);

        builder.comment("");
        builder.comment("  ~~~ [ Misc Configs] ~~~");
        builder.comment("");

        noTrample = builder.comment("Prevents farmland from being trampled by players or entities").define("no_trample", true);

        sturdyFarmland = builder.comment("Farmland will not check for blocks above it, and can be built on top of without turning to dirt").define("sturdy_farmland", true);

        shovelReverting = builder.comment("Allows shovels to turn farmland to dirt when right clicking").define("shovel_reverting", true);


    }
}