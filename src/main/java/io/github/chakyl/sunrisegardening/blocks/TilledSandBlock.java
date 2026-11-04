package io.github.chakyl.sunrisegardening.blocks;

import com.mojang.serialization.MapCodec;
import cool.bot.botslib.util.RNG;
import io.github.chakyl.sunrisegardening.SunriseGardening;
import io.github.chakyl.sunrisegardening.dataattachments.DayData;
import io.github.chakyl.sunrisegardening.registry.SunriseRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import org.jetbrains.annotations.Nullable;

import static io.github.chakyl.sunrisegardening.util.CropHandlerUtils.processDailyLogic;
import static io.github.chakyl.sunrisegardening.util.DayDataUtils.*;

public class TilledSandBlock extends FallingBlock {
    public static final VoxelShape SHAPE = box(0, 0, 0, 16, 16, 16);

    public TilledSandBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        LevelChunk chunk = level.getChunkAt(pos);
        BlockPos abovePos = pos.above();
        long currentDay = getCurrentDay(level);
        DayData dayData = getOrCreateDayData(level, pos);
        long daysPassed = Math.max(0, currentDay - dayData.dayLastGrown);

        if (compareDay(currentDay, dayData.dayLastGrown, 1)) {
            BlockState crop = level.getBlockState(abovePos);
            boolean becameSand = false;
            if (crop.is(Blocks.AIR) && RNG.mc_ihundo(random, SunriseGardening.CONFIG.dailyCroplessDecayChance.get())) {
                becameSand = true;
            } else if (RNG.mc_ihundo(random, SunriseGardening.CONFIG.dailyCropDecayChance.get())) {
                becameSand = true;
            } else {
                if (!(crop.getBlock() instanceof AirBlock)) {
                    processDailyLogic(level, pos, state, daysPassed, dayData, true);
                }
            }
            if (becameSand) level.setBlock(pos, Blocks.SAND.defaultBlockState(), 3);

            dayData.dayLastGrown = currentDay;

            chunk.setUnsaved(true);
        }

        scheduleNextTick(level, pos);
    }


    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if (!level.isClientSide && !oldState.is(state.getBlock())) {
            LevelChunk chunk = level.getChunkAt(pos);
            chunk.getData(SunriseRegistry.AttachmentRegistry.TILLED_BLOCK_CHUNK_DATA).dayDataMap.put(pos, new DayData(getCurrentDay(level), 0));
            chunk.setUnsaved(true);
            scheduleNextTick(level, pos);
        }
        super.onPlace(state, level, pos, oldState, movedByPiston);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!level.isClientSide && !state.is(newState.getBlock())) {
            LevelChunk chunk = level.getChunkAt(pos);
            chunk.getData(SunriseRegistry.AttachmentRegistry.TILLED_BLOCK_CHUNK_DATA).dayDataMap.remove(pos);
            chunk.setUnsaved(true);
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }


    @Nullable
    @Override
    public BlockState getToolModifiedState(BlockState state, UseOnContext context, ItemAbility toolAbility, boolean simulate) {
        ItemStack itemStack = context.getItemInHand();

        if (!itemStack.canPerformAction(toolAbility)) {
            return null;
        }

        if (ItemAbilities.SHOVEL_FLATTEN.equals(toolAbility) && SunriseGardening.CONFIG.shovelReverting.getAsBoolean()) {
            return Blocks.SAND.defaultBlockState();
        }

        return super.getToolModifiedState(state, context, toolAbility, simulate);
    }


    private void scheduleNextTick(Level level, BlockPos blockPos) {
        level.scheduleTick(blockPos, this, 10);
    }


    @Override
    protected MapCodec<? extends FallingBlock> codec() {
        return null;
    }

}