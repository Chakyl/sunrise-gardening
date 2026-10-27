package io.github.chakyl.sunrisegardening.datagen;

import cool.bot.botslib.tag.DewDropBlockTags;
import io.github.chakyl.sunrisegardening.SunriseGardening;
import io.github.chakyl.sunrisegardening.registry.SunriseRegistry;
import io.github.chakyl.sunrisegardening.registry.SunriseTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,CompletableFuture<TagLookup<Block>> blockTags, ExistingFileHelper exFileHelper) {
        super(output, lookupProvider, blockTags,  SunriseGardening.MODID, exFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        this.registerMinecraftTags();
    }

    protected void registerMinecraftTags() {
        generateFertilizerTag(SunriseRegistry.ItemRegistry.BOUNTIFUL_FERTILIZER);
        generateFertilizerTag(SunriseRegistry.ItemRegistry.HYDRATING_FERTILIZER, SunriseTags.HYDRATING_FERTILIZER);
        generateFertilizerTag(SunriseRegistry.ItemRegistry.DELUXE_HYDRATING_FERTILIZER, SunriseTags.HYDRATING_FERTILIZER);
        generateFertilizerTag(SunriseRegistry.ItemRegistry.LOW_QUALITY_FERTILIZER, SunriseTags.QUALITY_FERTILIZER);
        generateFertilizerTag(SunriseRegistry.ItemRegistry.HIGH_QUALITY_FERTILIZER, SunriseTags.QUALITY_FERTILIZER);
        generateFertilizerTag(SunriseRegistry.ItemRegistry.PRISTINE_QUALITY_FERTILIZER, SunriseTags.QUALITY_FERTILIZER);
        generateFertilizerTag(SunriseRegistry.ItemRegistry.WEAK_FERTILIZER, SunriseTags.SPEED_FERTILIZER);
        generateFertilizerTag(SunriseRegistry.ItemRegistry.STRONG_FERTILIZER, SunriseTags.SPEED_FERTILIZER);
        generateFertilizerTag(SunriseRegistry.ItemRegistry.HYPER_FERTILIZER, SunriseTags.SPEED_FERTILIZER);
    }

    public void generateFertilizerTag(DeferredHolder<Item, Item> item) {
        generateFertilizerTag(item, null);
    }

    public void generateFertilizerTag(DeferredHolder<Item, Item> item, TagKey<Item> typeTag) {
        tag(SunriseTags.FERTILIZER).add(item.get());
        if (typeTag != null) tag(typeTag).add(item.get());
    }


}