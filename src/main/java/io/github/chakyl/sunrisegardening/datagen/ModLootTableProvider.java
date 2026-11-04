package io.github.chakyl.sunrisegardening.datagen;


import io.github.chakyl.sunrisegardening.registry.SunriseRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ModLootTableProvider extends BlockLootSubProvider {
    public ModLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        dropOther(SunriseRegistry.BlockRegistry.TILLED_SOIL.get(), Items.DIRT);
        dropOther(SunriseRegistry.BlockRegistry.BOUNTIFUL_FERTILIZED_FARMLAND.get(), Items.DIRT);
        dropOther(SunriseRegistry.BlockRegistry.HYDRATING_FARMLAND.get(), Items.DIRT);
        dropOther(SunriseRegistry.BlockRegistry.DELUXE_HYDRATING_FARMLAND.get(), Items.DIRT);
        dropOther(SunriseRegistry.BlockRegistry.LOW_QUALITY_FERTILIZED_FARMLAND.get(), Items.DIRT);
        dropOther(SunriseRegistry.BlockRegistry.HIGH_QUALITY_FERTILIZED_FARMLAND.get(), Items.DIRT);
        dropOther(SunriseRegistry.BlockRegistry.PRISTINE_QUALITY_FERTILIZED_FARMLAND.get(), Items.DIRT);
        dropOther(SunriseRegistry.BlockRegistry.WEAK_FERTILIZED_FARMLAND.get(), Items.DIRT);
        dropOther(SunriseRegistry.BlockRegistry.STRONG_FERTILIZED_FARMLAND.get(), Items.DIRT);
        dropOther(SunriseRegistry.BlockRegistry.HYPER_FERTILIZED_FARMLAND.get(), Items.DIRT);
        dropOther(SunriseRegistry.BlockRegistry.TILLED_SAND.get(), Items.SAND);
        dropOther(SunriseRegistry.BlockRegistry.HYPER_FERTILIZED_SAND.get(), Items.SAND);
        dropSelf(SunriseRegistry.BlockRegistry.GARDEN_POT.get());
        dropOther(SunriseRegistry.BlockRegistry.IRON_HANGING_GARDEN_POT.get(), SunriseRegistry.BlockRegistry.GARDEN_POT.get().asItem());
        dropOther(SunriseRegistry.BlockRegistry.ROPE_HANGING_GARDEN_POT.get(), SunriseRegistry.BlockRegistry.GARDEN_POT.get().asItem());
        dropSelf(SunriseRegistry.BlockRegistry.IRON_SPRINKLER.get());
        dropSelf(SunriseRegistry.BlockRegistry.GOLD_SPRINKLER.get());
        dropSelf(SunriseRegistry.BlockRegistry.DIAMOND_SPRINKLER.get());
        dropSelf(SunriseRegistry.BlockRegistry.NETHERITE_SPRINKLER.get());


    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        List<Block> list = new ArrayList<>();
        list.add(SunriseRegistry.BlockRegistry.TILLED_SOIL.get());
        list.add(SunriseRegistry.BlockRegistry.BOUNTIFUL_FERTILIZED_FARMLAND.get());
        list.add(SunriseRegistry.BlockRegistry.HYDRATING_FARMLAND.get());
        list.add(SunriseRegistry.BlockRegistry.DELUXE_HYDRATING_FARMLAND.get());
        list.add(SunriseRegistry.BlockRegistry.LOW_QUALITY_FERTILIZED_FARMLAND.get());
        list.add(SunriseRegistry.BlockRegistry.HIGH_QUALITY_FERTILIZED_FARMLAND.get());
        list.add(SunriseRegistry.BlockRegistry.PRISTINE_QUALITY_FERTILIZED_FARMLAND.get());
        list.add(SunriseRegistry.BlockRegistry.WEAK_FERTILIZED_FARMLAND.get());
        list.add(SunriseRegistry.BlockRegistry.STRONG_FERTILIZED_FARMLAND.get());
        list.add(SunriseRegistry.BlockRegistry.HYPER_FERTILIZED_FARMLAND.get());
        list.add(SunriseRegistry.BlockRegistry.TILLED_SAND.get());
        list.add(SunriseRegistry.BlockRegistry.HYPER_FERTILIZED_SAND.get());
        list.add(SunriseRegistry.BlockRegistry.GARDEN_POT.get());
        list.add(SunriseRegistry.BlockRegistry.IRON_HANGING_GARDEN_POT.get());
        list.add(SunriseRegistry.BlockRegistry.ROPE_HANGING_GARDEN_POT.get());
        list.add(SunriseRegistry.BlockRegistry.IRON_SPRINKLER.get());
        list.add(SunriseRegistry.BlockRegistry.GOLD_SPRINKLER.get());
        list.add(SunriseRegistry.BlockRegistry.DIAMOND_SPRINKLER.get());
        list.add(SunriseRegistry.BlockRegistry.NETHERITE_SPRINKLER.get());
        return list::iterator;
    }
}