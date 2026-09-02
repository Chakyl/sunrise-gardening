package io.github.chakyl.sunrisegardening.registry;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.datamaps.DataMapType;

import static io.github.chakyl.sunrisegardening.SunriseGardening.loc;

public class DataMapRegistry {
    public static final DataMapType<Block, Integer> MULTI_HARVEST_CROPS_DATA = DataMapType.builder(
            loc("multi_harvest_crops"),
            Registries.BLOCK,
            Codec.INT
    ).build();

    public static final DataMapType<Block, Integer> CROP_GROW_TIMES = DataMapType.builder(
            loc("crop_grow_times"),
            Registries.BLOCK,
            Codec.INT
    ).build();

    public static final DataMapType<Block, Integer> SPRINKLER_SIZE = DataMapType.builder(
            loc("sprinkler_size"),
            Registries.BLOCK,
            Codec.INT
    ).build();

    public static final DataMapType<Block, Integer> GROWTH_FERTILIZER_RATE = DataMapType.builder(
            loc("growth_fertilizer_rate"),
            Registries.BLOCK,
            Codec.INT
    ).build();

    public static final DataMapType<Item, Integer> HOE_AOE_RADIUS = DataMapType.builder(
            loc("hoe_aoe_radius"),
            Registries.ITEM,
            Codec.INT
    ).build();

    public static final DataMapType<Biome, String> PERMANENT_SEASON_BIOMES = DataMapType.builder(
            loc("permanent_season_biomes"),
            Registries.BIOME,
            Codec.STRING
    ).build();
}
