package io.github.chakyl.sunrisegardening.mixin;

import com.rosemods.windswept.common.block.WildBerryBushBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static io.github.chakyl.sunrisegardening.util.DayDataUtils.getOrCreateDayData;
import static io.github.chakyl.sunrisegardening.util.DayDataUtils.syncDayData;


@Mixin(WildBerryBushBlock.class)
public class WildBerryBushBlockHarvestMixin {
    @Inject(method = "useWithoutItem", at = @At(value = "RETURN", ordinal = 0))
    private void harvestBush(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (!level.isClientSide() && level instanceof ServerLevel serverLevel) {
            syncDayData(serverLevel, pos.below(),level.getBlockState(pos));
        }
    }
}