package io.github.chakyl.sunrisegardening.event;

import io.github.chakyl.sunrisegardening.SunriseGardening;
import io.github.chakyl.sunrisegardening.registry.SunriseRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.ChainBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import vectorwing.farmersdelight.common.tag.ModTags;

public class GardenPotEvents {
    @EventBusSubscriber(modid = SunriseGardening.MODID)
    public static class ForgeEvents {

        @SubscribeEvent
        public static void onBlockInteract(PlayerInteractEvent.RightClickBlock event) {
            Player player = event.getEntity();
            BlockPos blockPos = event.getPos();
            Level level = event.getLevel();
            BlockState clickedBlockState = level.getBlockState(blockPos);
            ItemStack stack = player.getItemInHand(event.getHand());
            if (stack.getItem() == SunriseRegistry.BlockRegistry.GARDEN_POT.get().asItem()) {
                if (level.getBlockState(blockPos.below()).getBlock() instanceof AirBlock && level.getBlockState(blockPos.below().below()).getBlock() instanceof AirBlock) {
                    if (clickedBlockState.getBlock() instanceof ChainBlock) {
                        if (!level.isClientSide) {
                            level.setBlock(blockPos.below().below(), SunriseRegistry.BlockRegistry.IRON_HANGING_GARDEN_POT.get().defaultBlockState(), 3);
                            if (!player.isCreative()) stack.shrink(1);
                            level.playSound(null, blockPos, SoundEvents.DECORATED_POT_PLACE, SoundSource.BLOCKS, 1.0f, 1.0f);
                        }
                        event.setCanceled(true);
                        event.setCancellationResult(InteractionResult.SUCCESS);
                    } else if (SunriseGardening.FARMERS_DELIGHT_INSTALLED && clickedBlockState.is(ModTags.Blocks.ROPES)) {
                        if (!level.isClientSide) {
                            level.setBlock(blockPos.below().below(), SunriseRegistry.BlockRegistry.ROPE_HANGING_GARDEN_POT.get().defaultBlockState(), 3);
                            if (!player.isCreative()) stack.shrink(1);
                            level.playSound(null, blockPos, SoundEvents.DECORATED_POT_PLACE, SoundSource.BLOCKS, 1.0f, 1.0f);
                        }
                        event.setCanceled(true);
                        event.setCancellationResult(InteractionResult.SUCCESS);
                    }
                }
            }
        }
    }
}