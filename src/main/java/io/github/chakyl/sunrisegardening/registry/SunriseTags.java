package io.github.chakyl.sunrisegardening.registry;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.BlockTags;

import static io.github.chakyl.sunrisegardening.SunriseGardening.loc;


public class SunriseTags {
    // Farmland
    public static final TagKey<Block> WEAK_FERTILIZED_FARMLAND = tag("weak_fertilized_farmland");
    public static final TagKey<Block> STRONG_FERTILIZED_FARMLAND = tag("strong_fertilized_farmland");
    public static final TagKey<Block> HYPER_FERTILIZED_FARMLAND = tag("hyper_fertilized_farmland");
    public static final TagKey<Block> BOUNTIFUL_FERTILIZED_FARMLAND = tag("bountiful_fertilized_farmland");
    public static final TagKey<Block> HYDRATING_FARMLAND = tag("hydrating_farmland");
    public static final TagKey<Block> DELUXE_HYDRATING_FARMLAND = tag("deluxe_hydrating_farmland");
    public static final TagKey<Block> LOW_QUALITY_FERTILIZED_FARMLAND = tag("low_quality_fertilized_farmland");
    public static final TagKey<Block> HIGH_QUALITY_FERTILIZED_FARMLAND = tag("high_quality_farmland");
    public static final TagKey<Block> PRISTINE_QUALITY_FERTILIZED_FARMLAND = tag("pristine_quality_farmland");

    // Misc
    public static final TagKey<Block> TURNS_INTO_TILLED_SOIL = tag("turns_into_tilled_soil");
    public static final TagKey<Block> CANCEL_RANDOM_TICK = tag("cancel_random_tick");
    public static final TagKey<Block> NOT_DAILY = tag("not_daily");
    public static final TagKey<Block> SINGLE_HARVEST_CROP = tag("single_harvest_crop");
    public static final TagKey<Block> SPRINKLER = tag("sprinkler");

    public static TagKey<Block> tag(String name) {
        return BlockTags.create(loc(name));
    }
}