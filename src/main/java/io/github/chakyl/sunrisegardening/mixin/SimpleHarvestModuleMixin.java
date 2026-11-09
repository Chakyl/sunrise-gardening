package io.github.chakyl.sunrisegardening.mixin;

import io.github.chakyl.sunrisegardening.util.SimpleHarvestUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.violetmoon.quark.content.tweaks.module.SimpleHarvestModule;

import static io.github.chakyl.sunrisegardening.util.CropHandlerUtils.getSoilPos;
import static io.github.chakyl.sunrisegardening.util.DayDataUtils.syncDayData;

@Mixin(value = SimpleHarvestModule.class, remap = false)
public class SimpleHarvestModuleMixin {

    @ModifyVariable(method = "harvestAndReplant", at = @At("STORE"), name = "newBlock")
    private static BlockState modifyReplantedBlock(BlockState original, Level level, BlockPos pos, BlockState inWorld) {
        return SimpleHarvestUtil.handleCropHarvest(level, pos, original);
    }

    @Inject(method = "harvestAndReplant", at = @At("TAIL"))
    private static void syncCropChunkData(Level level, BlockPos pos, BlockState inWorld, LivingEntity entity, InteractionHand hand, CallbackInfoReturnable<Boolean> cir) {
        if (!level.isClientSide && cir.getReturnValue()) {
            syncDayData((ServerLevel) level, getSoilPos(level, pos), level.getBlockState(pos));
        }
    }
}