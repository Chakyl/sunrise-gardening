package io.github.chakyl.sunrisegardening.registry;

import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.BlockTags;

import static io.github.chakyl.sunrisegardening.SunriseGardening.loc;


public class SunriseTags {
    // Farmland
    public static final TagKey<Block> CROP_GROWING = blockTag("crop_growing");
    public static final TagKey<Block> WEAK_FERTILIZED_FARMLAND = blockTag("weak_fertilized_farmland");
    public static final TagKey<Block> STRONG_FERTILIZED_FARMLAND = blockTag("strong_fertilized_farmland");
    public static final TagKey<Block> HYPER_FERTILIZED_FARMLAND = blockTag("hyper_fertilized_farmland");
    public static final TagKey<Block> BOUNTIFUL_FERTILIZED_FARMLAND = blockTag("bountiful_fertilized_farmland");
    public static final TagKey<Block> HYDRATING_FARMLAND = blockTag("hydrating_farmland");
    public static final TagKey<Block> DELUXE_HYDRATING_FARMLAND = blockTag("deluxe_hydrating_farmland");
    public static final TagKey<Block> LOW_QUALITY_FERTILIZED_FARMLAND = blockTag("low_quality_fertilized_farmland");
    public static final TagKey<Block> HIGH_QUALITY_FERTILIZED_FARMLAND = blockTag("high_quality_farmland");
    public static final TagKey<Block> PRISTINE_QUALITY_FERTILIZED_FARMLAND = blockTag("pristine_quality_farmland");
    public static final TagKey<Block> GREENHOUSE_GLASS = blockTag("greenhouse_glass");

    // Item
    public static final TagKey<Item> FERTILIZER = itemTag("fertilizer");
    public static final TagKey<Item> SPEED_FERTILIZER = itemTag("speed_fertilizer");
    public static final TagKey<Item> QUALITY_FERTILIZER = itemTag("quality_fertilizer");
    public static final TagKey<Item> HYDRATING_FERTILIZER = itemTag("hydrating_fertilizer");
    // Misc
    public static final TagKey<Block> TURNS_INTO_TILLED_SOIL = blockTag("turns_into_tilled_soil");
    public static final TagKey<Block> CANCEL_RANDOM_TICK = blockTag("cancel_random_tick");
    public static final TagKey<Block> NOT_DAILY = blockTag("not_daily");
    public static final TagKey<Block> SINGLE_HARVEST_CROP = blockTag("single_harvest_crop");
    public static final TagKey<Block> SPRINKLER = blockTag("sprinkler");

    public static TagKey<Block> blockTag(String name) {
        return BlockTags.create(loc(name));
    }

    public static TagKey<Item> itemTag(String name) {
        return ItemTags.create(loc(name));
    }
}