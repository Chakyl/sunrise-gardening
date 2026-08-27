package io.github.chakyl.sunrisegardening.items;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class FertilizerItem extends Item {
    public FertilizerItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if (!pContext.getLevel().isClientSide()) {
            ServerLevel level = (ServerLevel) pContext.getLevel();
            BlockPos posClicked = pContext.getClickedPos();
            BlockState state = level.getBlockState(posClicked);
            Player player = pContext.getPlayer();
            ItemStack stack = pContext.getItemInHand();

            boolean targetingCrop = state.is(BlockTags.CROPS) && level.getBlockState(posClicked.below()).is(Blocks.FARMLAND);


//            if (level.getBlockState(posClicked).is(Blocks.FARMLAND) || level.getBlockState(posClicked).is(SunnyRegistry.BlockRegistry.TILLED_SAND.get()) || level.getBlockState(posClicked).getBlock() instanceof GardenPotBlock || targetingCrop) {
//                if (targetingCrop) {
//                    posClicked = posClicked.below();
//                    state = level.getBlockState(posClicked);
//                }
//                if (level.getBlockState(posClicked).is(SunnyRegistry.BlockRegistry.TILLED_SAND.get())) {
//                    if (stack.is(SunnyRegistry.ItemRegistry.HYPER_FERTILIZER.get())) {
//                        level.setBlock(posClicked, SunnyRegistry.BlockRegistry.HYPER_FERTILIZED_SAND.get().defaultBlockState(), 3);
//                    } else {
//                        return InteractionResult.FAIL;
//                    }
//                } else if (level.getBlockState(posClicked).getBlock() instanceof GardenPotBlock){
//                    if (stack.is(SunnyRegistry.ItemRegistry.DELUXE_HYDRATING_FERTILIZER.get()) && !level.getBlockState(posClicked).getValue(GardenPotBlock.DELUXE)) {
//                        level.setBlock(posClicked, level.getBlockState(posClicked).setValue(GardenPotBlock.DELUXE, true), 3);
//                    } else {
//                        return InteractionResult.FAIL;
//                    }
//                }else {
//                    Item fertilizerItem = stack.getItem();
//                    Block farmland = switch (fertilizerItem) {
//                        case fertilizerItem == SunnyRegistry.ItemRegistry.WEAK_FERTILIZER.get() -> SunnyRegistry.BlockRegistry.WEAK_FERTILIZED_FARMLAND.get();
//                        case fertilizerItem == SunnyRegistry.ItemRegistry.STRONG_FERTILIZER.get() -> SunnyRegistry.BlockRegistry.STRONG_FERTILIZED_FARMLAND.get();
//                        case fertilizerItem == SunnyRegistry.ItemRegistry.HYPER_FERTILIZER.get() -> SunnyRegistry.BlockRegistry.HYPER_FERTILIZED_FARMLAND.get();
//                        case fertilizerItem == SunnyRegistry.ItemRegistry.HYDRATING_FERTILIZER.get() -> SunnyRegistry.BlockRegistry.HYDRATING_FARMLAND.get();
//                        case fertilizerItem == SunnyRegistry.ItemRegistry.DELUXE_HYDRATING_FERTILIZER.get() -> SunnyRegistry.BlockRegistry.DELUXE_HYDRATING_FARMLAND.get();
//                        case fertilizerItem == SunnyRegistry.ItemRegistry.BOUNTIFUL_FERTILIZER.get() -> SunnyRegistry.BlockRegistry.BOUNTIFUL_FERTILIZED_FARMLAND.get();
//                        case fertilizerItem == SunnyRegistry.ItemRegistry.LOW_QUALITY_FERTILIZER.get() -> SunnyRegistry.BlockRegistry.LOW_QUALITY_FERTILIZED_FARMLAND.get();
//                        case fertilizerItem == SunnyRegistry.ItemRegistry.HIGH_QUALITY_FERTILIZER.get() -> SunnyRegistry.BlockRegistry.HIGH_QUALITY_FERTILIZED_FARMLAND.get();
//                        case fertilizerItem == SunnyRegistry.ItemRegistry.PRISTINE_QUALITY_FERTILIZER.get() -> SunnyRegistry.BlockRegistry.PRISTINE_QUALITY_FERTILIZED_FARMLAND.get();
//                        default -> null;
//                    };
//
//                    if (farmland != null) {
//                        level.setBlock(posClicked, moisturizer(farmland, state), 3);
//                    } else {
//                        return InteractionResult.FAIL;
//                    }
//                }
//                if (player != null && !player.isCreative()) {
//                    stack.shrink(1);
//                }
//                level.playSound(null, posClicked.getX(), posClicked.getY(), posClicked.getZ(), SoundEvents.GRASS_HIT, SoundSource.BLOCKS, 1.0F, 0.8F);
//                return InteractionResult.SUCCESS;
//            }
        }
        return InteractionResult.FAIL;
    }

    private static BlockState moisturizer(Block block, BlockState state) {
        return block.defaultBlockState().setValue(BlockStateProperties.MOISTURE, state.getValue(BlockStateProperties.MOISTURE));
    }
}