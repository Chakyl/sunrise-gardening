package io.github.chakyl.sunrisegardening.items;

import io.github.chakyl.sunrisegardening.blocks.GardenPotBlock;
import io.github.chakyl.sunrisegardening.registry.SunriseRegistry;
import io.github.chakyl.sunrisegardening.registry.SunriseTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import static io.github.chakyl.sunrisegardening.util.DayDataUtils.getOrCreateDayData;
import static io.github.chakyl.sunrisegardening.util.DayDataUtils.syncDayData;

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

            boolean targetingCrop = state.is(BlockTags.CROPS) && level.getBlockState(posClicked.below()).is(SunriseRegistry.BlockRegistry.TILLED_SOIL);
// TODO: Put that in the code block before

            if (level.getBlockState(posClicked).is(SunriseRegistry.BlockRegistry.TILLED_SOIL) || level.getBlockState(posClicked).is(SunriseRegistry.BlockRegistry.TILLED_SAND.get()) || level.getBlockState(posClicked).getBlock() instanceof GardenPotBlock || targetingCrop) {
                if (targetingCrop) {
                    posClicked = posClicked.below();
                    state = level.getBlockState(posClicked);
                }
                if (level.getBlockState(posClicked).is(SunriseRegistry.BlockRegistry.TILLED_SAND.get())) {
                    if (stack.is(SunriseRegistry.ItemRegistry.HYPER_FERTILIZER.get())) {
                        level.setBlock(posClicked, SunriseRegistry.BlockRegistry.HYPER_FERTILIZED_SAND.get().defaultBlockState(), 3);
                    } else {
                        return InteractionResult.FAIL;
                    }
                } else if (level.getBlockState(posClicked).getBlock() instanceof GardenPotBlock) {
                    if (stack.is(SunriseRegistry.ItemRegistry.DELUXE_HYDRATING_FERTILIZER.get()) && !level.getBlockState(posClicked).getValue(GardenPotBlock.DELUXE)) {
                        level.setBlock(posClicked, level.getBlockState(posClicked).setValue(GardenPotBlock.DELUXE, true), 3);
                    } else {
                        return InteractionResult.FAIL;
                    }
                } else {
                    if (stack.is(SunriseTags.SPEED_FERTILIZER)) {
                        if (stack.is(SunriseRegistry.ItemRegistry.WEAK_FERTILIZER.get())) {
                            level.setBlock(posClicked, moisturizer(SunriseRegistry.BlockRegistry.WEAK_FERTILIZED_FARMLAND.value(), state), 3);
                        } else if (stack.is(SunriseRegistry.ItemRegistry.STRONG_FERTILIZER.get())) {
                            level.setBlock(posClicked, moisturizer(SunriseRegistry.BlockRegistry.STRONG_FERTILIZED_FARMLAND.value(), state), 3);
                        } else if (stack.is(SunriseRegistry.ItemRegistry.HYPER_FERTILIZER.get())) {
                            level.setBlock(posClicked, moisturizer(SunriseRegistry.BlockRegistry.HYPER_FERTILIZED_FARMLAND.value(), state), 3);
                        }
                        BlockState cropBlockState = level.getBlockState(posClicked.above());
                        syncDayData(getOrCreateDayData(level, posClicked), cropBlockState, state);
                    } else if (stack.is(SunriseRegistry.ItemRegistry.HYDRATING_FERTILIZER.get())) {
                        level.setBlock(posClicked, moisturizer(SunriseRegistry.BlockRegistry.HYDRATING_FARMLAND.value(), state), 3);
                    } else if (stack.is(SunriseRegistry.ItemRegistry.DELUXE_HYDRATING_FERTILIZER.get())) {
                        level.setBlock(posClicked, moisturizer(SunriseRegistry.BlockRegistry.DELUXE_HYDRATING_FARMLAND.value(), state), 3);
                    } else if (stack.is(SunriseRegistry.ItemRegistry.BOUNTIFUL_FERTILIZER.get())) {
                        level.setBlock(posClicked, moisturizer(SunriseRegistry.BlockRegistry.BOUNTIFUL_FERTILIZED_FARMLAND.value(), state), 3);
                    } else if (stack.is(SunriseRegistry.ItemRegistry.LOW_QUALITY_FERTILIZER.get())) {
                        level.setBlock(posClicked, moisturizer(SunriseRegistry.BlockRegistry.LOW_QUALITY_FERTILIZED_FARMLAND.value(), state), 3);
                    } else if (stack.is(SunriseRegistry.ItemRegistry.HIGH_QUALITY_FERTILIZER.get())) {
                        level.setBlock(posClicked, moisturizer(SunriseRegistry.BlockRegistry.HIGH_QUALITY_FERTILIZED_FARMLAND.value(), state), 3);
                    } else if (stack.is(SunriseRegistry.ItemRegistry.PRISTINE_QUALITY_FERTILIZER.get())) {
                        level.setBlock(posClicked, moisturizer(SunriseRegistry.BlockRegistry.PRISTINE_QUALITY_FERTILIZED_FARMLAND.value(), state), 3);
                    } else {
                        return InteractionResult.FAIL;
                    }
                }
                if (player != null && !player.isCreative()) {
                    stack.shrink(1);
                }
                level.playSound(null, posClicked.getX(), posClicked.getY(), posClicked.getZ(), SoundEvents.GRASS_HIT, SoundSource.BLOCKS, 1.0F, 0.8F);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.FAIL;
    }

    private static BlockState moisturizer(Block block, BlockState state) {
        return block.defaultBlockState().setValue(BlockStateProperties.MOISTURE, state.getValue(BlockStateProperties.MOISTURE));
    }
}