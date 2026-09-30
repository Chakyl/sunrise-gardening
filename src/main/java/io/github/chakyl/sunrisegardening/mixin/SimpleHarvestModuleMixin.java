package io.github.chakyl.sunrisegardening.mixin;

import io.github.chakyl.sunrisegardening.util.SimpleHarvestUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.violetmoon.quark.content.tweaks.module.SimpleHarvestModule;

import static io.github.chakyl.sunrisegardening.util.DayDataUtils.getDayData;
import static io.github.chakyl.sunrisegardening.util.DayDataUtils.syncDayData;

@Mixin(value = SimpleHarvestModule.class, remap = false)
public class SimpleHarvestModuleMixin {

    @ModifyVariable(method = "harvestAndReplant", at = @At("STORE"), name = "newBlock")
    private static BlockState modifyReplantedBlock(BlockState original, Level level, BlockPos pos, BlockState inWorld) {
        return SimpleHarvestUtil.handleCropHarvest(level, pos, original);
    }

    @Inject(method = "click(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/BlockHitResult;)Z", at = @At("TAIL"))
    private static void syncCropChunkData(Player player, InteractionHand hand, BlockPos pos, BlockHitResult pick, CallbackInfoReturnable<Boolean> cir) {        // Call your custom method here
        if (!player.level().isClientSide) {
            ServerLevel level = (ServerLevel) player.level();
            BlockState blockState = level.getBlockState(pos);
            if (blockState.getBlock() instanceof CropBlock cropBlock) {
                syncDayData(getDayData(level, pos.below()), blockState, cropBlock, level.getBlockState(pos.below()));
            }
        }
    }
}