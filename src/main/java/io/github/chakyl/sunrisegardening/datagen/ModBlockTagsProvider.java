package io.github.chakyl.sunrisegardening.datagen;

import cool.bot.botslib.tag.DewDropBlockTags;
import io.github.chakyl.sunrisegardening.SunriseGardening;
import io.github.chakyl.sunrisegardening.registry.SunriseRegistry;
import io.github.chakyl.sunrisegardening.registry.SunriseTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper exFileHelper) {
        super(output, lookupProvider, SunriseGardening.MODID, exFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        this.registerMinecraftTags();
    }

    protected void registerMinecraftTags() {
        generateFarmlandTags(SunriseRegistry.BlockRegistry.TILLED_SOIL, null);
        generateFarmlandTags(SunriseRegistry.BlockRegistry.BOUNTIFUL_FERTILIZED_FARMLAND, SunriseTags.BOUNTIFUL_FERTILIZED_FARMLAND);
        generateFarmlandTags(SunriseRegistry.BlockRegistry.HYDRATING_FARMLAND, SunriseTags.HYDRATING_FARMLAND);
        generateFarmlandTags(SunriseRegistry.BlockRegistry.DELUXE_HYDRATING_FARMLAND, SunriseTags.DELUXE_HYDRATING_FARMLAND);
        generateFarmlandTags(SunriseRegistry.BlockRegistry.LOW_QUALITY_FERTILIZED_FARMLAND, SunriseTags.LOW_QUALITY_FERTILIZED_FARMLAND);
        generateFarmlandTags(SunriseRegistry.BlockRegistry.HIGH_QUALITY_FERTILIZED_FARMLAND, SunriseTags.HIGH_QUALITY_FERTILIZED_FARMLAND);
        generateFarmlandTags(SunriseRegistry.BlockRegistry.PRISTINE_QUALITY_FERTILIZED_FARMLAND, SunriseTags.PRISTINE_QUALITY_FERTILIZED_FARMLAND);
        generateFarmlandTags(SunriseRegistry.BlockRegistry.WEAK_FERTILIZED_FARMLAND, SunriseTags.WEAK_FERTILIZED_FARMLAND);
        generateFarmlandTags(SunriseRegistry.BlockRegistry.STRONG_FERTILIZED_FARMLAND, SunriseTags.STRONG_FERTILIZED_FARMLAND);
        generateFarmlandTags(SunriseRegistry.BlockRegistry.HYPER_FERTILIZED_FARMLAND, SunriseTags.HYPER_FERTILIZED_FARMLAND);

        generateGardenPotTags(SunriseRegistry.BlockRegistry.GARDEN_POT);
        generateGardenPotTags(SunriseRegistry.BlockRegistry.ROPE_HANGING_GARDEN_POT);
        generateGardenPotTags(SunriseRegistry.BlockRegistry.IRON_HANGING_GARDEN_POT);

        generateSprinklerTag(SunriseRegistry.BlockRegistry.IRON_SPRINKLER);
        generateSprinklerTag(SunriseRegistry.BlockRegistry.GOLD_SPRINKLER);
        generateSprinklerTag(SunriseRegistry.BlockRegistry.DIAMOND_SPRINKLER);
        generateSprinklerTag(SunriseRegistry.BlockRegistry.NETHERITE_SPRINKLER);

        generateGreenhouseGlassTag(SunriseRegistry.BlockRegistry.GREENHOUSE_GLASS);
        generateGreenhouseGlassTag(SunriseRegistry.BlockRegistry.GREENHOUSE_GLASS_STAIRS);
        generateGreenhouseGlassTag(SunriseRegistry.BlockRegistry.GREENHOUSE_GLASS_SLAB);
        generateGreenhouseGlassTag(SunriseRegistry.BlockRegistry.GREENHOUSE_GLASS_PANE);
    }

    public void generateFarmlandTags(DeferredHolder<Block, Block> block, TagKey<Block> farmlandTag) {
        generateFarmlandTags(block, farmlandTag, BlockTags.MINEABLE_WITH_SHOVEL);
    }

    public void generateFarmlandTags(DeferredHolder<Block, Block> block, TagKey<Block> farmlandTag, TagKey<Block> mineableTag) {
        tag(DewDropBlockTags.WATERABLE).add(block.get());
        tag(SunriseTags.CROP_GROWING).add(block.get());
        if (farmlandTag != null) tag(farmlandTag).add(block.get());
        tag(mineableTag).add(block.get());
    }

    public void generateGardenPotTags(DeferredHolder<Block, Block> block) {
        tag(DewDropBlockTags.WATERABLE).add(block.get());
        tag(SunriseTags.CROP_GROWING).add(block.get());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(block.get());
    }

    public void generateSprinklerTag(DeferredHolder<Block, Block> block) {
        tag(SunriseTags.SPRINKLER).add(block.get());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(block.get());
    }

    public void generateGreenhouseGlassTag(DeferredHolder<Block, Block> block) {
        tag(SunriseTags.GREENHOUSE_GLASS).add(block.get());
    }
}