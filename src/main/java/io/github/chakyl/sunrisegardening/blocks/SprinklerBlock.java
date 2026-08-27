package io.github.chakyl.sunrisegardening.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class SprinklerBlock extends Block {
    public static final VoxelShape SHAPE = box(2, 0, 2, 14, 9, 14);
    public static final VoxelShape SHAPE_STICKLOGGED = box(2, 0, 2, 14, 16, 14);

    public static final BooleanProperty STICKLOGGED = BooleanProperty.create("sticklogged");

    public SprinklerBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(STICKLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(STICKLOGGED);
    }
    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return pState.getValue(STICKLOGGED) ? SHAPE_STICKLOGGED : SHAPE;
    }

    @Override
    public @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if (pHand == InteractionHand.MAIN_HAND) {
            if (pPlayer.getItemInHand(pHand).is(Items.STICK) && !pState.getValue(STICKLOGGED)) {
                pLevel.setBlockAndUpdate(pPos, pState.setValue(STICKLOGGED, true));
                if (!pPlayer.isCreative()) pPlayer.getItemInHand(pHand).shrink(1);
                pPlayer.swing(pHand);
                return ItemInteractionResult.CONSUME;
            } else if (pPlayer.isCrouching() && pPlayer.getItemInHand(pHand).isEmpty() && pState.getValue(STICKLOGGED)) {
                pLevel.setBlockAndUpdate(pPos, pState.setValue(STICKLOGGED, false));
                pPlayer.addItem(Items.STICK.getDefaultInstance());
                pPlayer.swing(pHand);
                return ItemInteractionResult.CONSUME;
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
}