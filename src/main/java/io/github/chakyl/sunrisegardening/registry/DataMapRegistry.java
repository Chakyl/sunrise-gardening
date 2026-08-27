package io.github.chakyl.sunrisegardening.registry;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.datamaps.DataMapType;

import static io.github.chakyl.sunrisegardening.SunriseGardening.loc;

public class DataMapRegistry {
    public static final DataMapType<Block, Integer> MULTI_HARVEST_CROPS_DATA = DataMapType.builder(
            loc("multi_harvest_crops"),
            Registries.BLOCK,
            Codec.INT
    ).build();
}
