package io.github.chakyl.sunrisegardening.registry;

import com.google.common.base.Suppliers;
import io.github.chakyl.sunrisegardening.SunriseGardening;
import io.github.chakyl.sunrisegardening.blocks.SprinklerBlock;
import io.github.chakyl.sunrisegardening.blocks.TilledSoilBlock;
import io.github.chakyl.sunrisegardening.dataattachments.TilledBlockChunkData;
import io.github.chakyl.sunrisegardening.items.FertilizerItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public final class SunriseRegistry {

    private static final String MODID = SunriseGardening.MODID;

    private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MODID);
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MODID);

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        BLOCK_ENTITY_TYPES.register(modEventBus);
        ATTACHMENT_TYPES.register(modEventBus);

        // Force initialization of all registry holder classes
        // This ensures all static fields are initialized during the registration phase
        BlockRegistry.init();
        BlockEntityRegistry.init();
        AttachmentRegistry.init();
        ItemRegistry.init();
        CreativeTabReg.init();
    }

    public static final class BlockRegistry {
        public static void init() {
        }
        public static final DeferredHolder<Block, Block> TILLED_SOIL = registerWithItem("tilled_soil",
                () -> new TilledSoilBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.FARMLAND)));
        public static final DeferredHolder<Block, Block>WEAK_FERTILIZED_FARMLAND = registerWithItem("weak_fertilized_farmland",
                () -> new TilledSoilBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.FARMLAND)));
        public static final DeferredHolder<Block, Block>STRONG_FERTILIZED_FARMLAND = registerWithItem("strong_fertilized_farmland",
                () -> new TilledSoilBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.FARMLAND)));
        public static final DeferredHolder<Block, Block>HYPER_FERTILIZED_FARMLAND = registerWithItem("hyper_fertilized_farmland",
                () -> new TilledSoilBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.FARMLAND)));
        public static final DeferredHolder<Block, Block>HYDRATING_FARMLAND = registerWithItem("hydrating_farmland",
                () -> new TilledSoilBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.FARMLAND)));
        public static final DeferredHolder<Block, Block>DELUXE_HYDRATING_FARMLAND = registerWithItem("deluxe_hydrating_farmland",
                () -> new TilledSoilBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.FARMLAND)));
        public static final DeferredHolder<Block, Block>BOUNTIFUL_FERTILIZED_FARMLAND = registerWithItem("bountiful_fertilized_farmland",
                () -> new TilledSoilBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.FARMLAND)));
        public static final DeferredHolder<Block, Block>LOW_QUALITY_FERTILIZED_FARMLAND = registerWithItem("low_quality_fertilized_farmland",
                () -> new TilledSoilBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.FARMLAND)));
        public static final DeferredHolder<Block, Block>HIGH_QUALITY_FERTILIZED_FARMLAND = registerWithItem("high_quality_fertilized_farmland",
                () -> new TilledSoilBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.FARMLAND)));
        public static final DeferredHolder<Block, Block>PRISTINE_QUALITY_FERTILIZED_FARMLAND = registerWithItem("pristine_quality_fertilized_farmland",
                () -> new TilledSoilBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.FARMLAND)));

        
        public static final DeferredHolder<Block, Block> IRON_SPRINKLER = registerWithItem("iron_sprinkler",
                () -> new SprinklerBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).sound(SoundType.COPPER).noOcclusion().strength(1F, 2.0F)));
        public static final DeferredHolder<Block, Block> GOLD_SPRINKLER = registerWithItem("gold_sprinkler",
                () -> new SprinklerBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).sound(SoundType.COPPER).noOcclusion().strength(1F, 2.0F)));
        public static final DeferredHolder<Block, Block> DIAMOND_SPRINKLER = registerWithItem("diamond_sprinkler",
                () -> new SprinklerBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).sound(SoundType.COPPER).noOcclusion().strength(1F, 2.0F)));
        public static final DeferredHolder<Block, Block> NETHERITE_SPRINKLER = registerWithItem("netherite_sprinkler",
                () -> new SprinklerBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).sound(SoundType.COPPER).noOcclusion().strength(1F, 2.0F)));

        private static boolean never(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
            return false;
        }

        private static DeferredHolder<Block, Block> registerWithItem(final String name, final Supplier<Block> supplier) {
            return registerWithItem(name, supplier, ItemRegistry::registerBlockItem);
        }

        private static DeferredHolder<Block, Block> registerWithItem(final String name, final Supplier<Block> blockSupplier, final Function<DeferredHolder<Block, Block>, DeferredHolder<Item, Item>> itemSupplier) {
            final DeferredHolder<Block, Block> block = BLOCKS.register(name, blockSupplier);
            final DeferredHolder<Item, Item> item = itemSupplier.apply(block);
            return block;
        }
    }

    public static final class BlockEntityRegistry {
        public static void init() {
        }
//        public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CowbellBlockEntity>> COWBELL = BLOCK_ENTITY_TYPES.register("cowbell",
//                () -> BlockEntityType.Builder.of(CowbellBlockEntity::new, BlockRegistry.COWBELL.get()).build(null));
    }


    public static final class ItemRegistry {
        public static void init() {
        }
//        public static final DeferredHolder<Item, Item> GATCHA_CAPSULE = ITEMS.register("gatcha_capsule", () -> new GatchaCapsuleItem(new Item.Properties().component(DataComponents.RARITY, Rarity.RARE).stacksTo(64)));

        private static final List<DeferredHolder<Item, Item>> ALL_ITEMS = new ArrayList<>();

        public static final DeferredHolder<Item, Item> WEAK_FERTILIZER = register("weak_fertilizer", () -> new FertilizerItem(new Item.Properties().stacksTo(64)));
        public static final DeferredHolder<Item, Item> STRONG_FERTILIZER = register("strong_fertilizer", () -> new FertilizerItem(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(64)));
        public static final DeferredHolder<Item, Item> HYPER_FERTILIZER = register("hyper_fertilizer", () -> new FertilizerItem(new Item.Properties().rarity(Rarity.RARE).stacksTo(64)));
        public static final DeferredHolder<Item, Item> HYDRATING_FERTILIZER = register("hydrating_fertilizer", () -> new FertilizerItem(new Item.Properties().stacksTo(64)));
        public static final DeferredHolder<Item, Item> DELUXE_HYDRATING_FERTILIZER = register("deluxe_hydrating_fertilizer", () -> new FertilizerItem(new Item.Properties().stacksTo(64)));
        public static final DeferredHolder<Item, Item> BOUNTIFUL_FERTILIZER = register("bountiful_fertilizer", () -> new FertilizerItem(new Item.Properties().stacksTo(64)));
        public static final DeferredHolder<Item, Item> LOW_QUALITY_FERTILIZER = register("low_quality_fertilizer", () -> new FertilizerItem(new Item.Properties().stacksTo(64)));
        public static final DeferredHolder<Item, Item> HIGH_QUALITY_FERTILIZER = register("high_quality_fertilizer", () -> new FertilizerItem(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(64)));
        public static final DeferredHolder<Item, Item> PRISTINE_QUALITY_FERTILIZER = register("pristine_quality_fertilizer", () -> new FertilizerItem(new Item.Properties().rarity(Rarity.RARE).stacksTo(64)));

        /**
         * Creates a registry object for a block item and adds it to the mod creative tab
         *
         * @param block the block
         * @return the registry object
         */
        private static DeferredHolder<Item, Item> registerBlockItem(final DeferredHolder<Block, Block> block) {
            return register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
        }

        /**
         * Creates a registry object for the given item and adds it to the mod creative tab
         *
         * @param name     the registry name
         * @param supplier the item supplier
         * @return the item registry object
         */
        private static DeferredHolder<Item, Item> register(final String name, final Supplier<Item> supplier) {
            final DeferredHolder<Item, Item> item = ITEMS.register(name, supplier);
            ALL_ITEMS.add(item);
            return item;
        }
    }

    public static final class AttachmentRegistry {
        public static void init() {
        }

        public static final Supplier<AttachmentType<TilledBlockChunkData>> TILLED_BLOCK_CHUNK_DATA = ATTACHMENT_TYPES.register("tilled_block_chunk_data", () -> AttachmentType.builder(TilledBlockChunkData::new).serialize(TilledBlockChunkData.CODEC).build());

    }


    public static final class CreativeTabReg {
        public static void init() {
        }


        public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB = CREATIVE_MODE_TABS.register("tab", () -> CreativeModeTab.builder()
                .icon(Suppliers.memoize(() -> new ItemStack(ItemRegistry.BOUNTIFUL_FERTILIZER.get())))
                .title(Component.translatable("itemGroup." + SunriseGardening.MODID))
                .withSearchBar()
                .displayItems((parameters, output) ->
                        output.acceptAll(ItemRegistry.ALL_ITEMS
                                .stream()
                                .map(o -> new ItemStack(o.get()))
                                .toList())
                )
                .build()
        );
    }

}