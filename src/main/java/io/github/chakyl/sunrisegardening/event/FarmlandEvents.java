package io.github.chakyl.sunrisegardening.event;

import cool.bot.botslib.tag.DewDropBlockTags;
import cool.bot.botslib.util.Util;
import io.github.chakyl.sunrisegardening.SunriseGardening;
import io.github.chakyl.sunrisegardening.registry.DataMapRegistry;
import io.github.chakyl.sunrisegardening.registry.SunriseRegistry;
import io.github.chakyl.sunrisegardening.registry.SunriseTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.util.Optional;

import static io.github.chakyl.sunrisegardening.util.HoeUtils.getHoeArea;

@EventBusSubscriber(modid = SunriseGardening.MODID)
public class FarmlandEvents {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        ItemStack stack = event.getItemStack();
        Player player = event.getEntity();
        hoeBlocks(event, stack, level, pos, player, SunriseTags.TURNS_INTO_TILLED_SOIL, SunriseRegistry.BlockRegistry.TILLED_SOIL.get().defaultBlockState());
        hoeBlocks(event, stack, level, pos, player, SunriseTags.TURNS_INTO_TILLED_SAND, SunriseRegistry.BlockRegistry.TILLED_SAND.get().defaultBlockState());
    }

    private static void hoeBlocks(PlayerInteractEvent.RightClickBlock event, ItemStack stack, Level level, BlockPos pos, Player player, TagKey<Block> blockTag, BlockState outputState) {
        if (stack.getItem() instanceof HoeItem && level.getBlockState(pos).is(blockTag)) {
            if (player.getCooldowns().isOnCooldown(stack.getItem())) return;
            if (!level.isClientSide) {
                level.setBlock(pos, outputState, 3);
                int durability = 1;
                if (player.isCrouching()) {
                    Integer aoeStyle = stack.getItemHolder().getData(DataMapRegistry.HOE_AOE_STYLE);
                    if (aoeStyle != null && aoeStyle >= 1 && aoeStyle <= 4) {
                        AABB area = getHoeArea(aoeStyle, player, pos);
                        BlockPos.betweenClosedStream(area).forEach(blockPos -> {
                            if (level.getBlockState(blockPos).is(blockTag)) level.setBlock(blockPos, outputState, 3);
                        });
                        durability += aoeStyle * 2;
                        player.getCooldowns().addCooldown(stack.getItem(), aoeStyle * 10);
                    }
                }
                if (player instanceof ServerPlayer serverPlayer) {
                    stack.hurtAndBreak(durability, serverPlayer, LivingEntity.getSlotForHand(event.getHand()));
                }
                level.playSound(null, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide()));
            event.setCanceled(true);
        }
    }


    @SubscribeEvent
    public static void onSplashWaterBottleHitEvent(ProjectileImpactEvent event) {
        if (!SunriseGardening.CONFIG.splashPotionWatering.get()) {
            return;
        }

        if (event.getProjectile() instanceof ThrownPotion) {
            ThrownPotion potion = ((ThrownPotion) event.getProjectile());
            Optional<Holder<Potion>> contents = potion.getItem().getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).potion();
            Potion type = contents.map(Holder::value).orElseGet(Potions.WATER::value);
            if (type.getEffects().isEmpty() || type == Potions.WATER) {
                if (event.getRayTraceResult().getType() == HitResult.Type.BLOCK) {
                    Level level = (event.getProjectile().getOwner().level());
                    if (level.isClientSide()) {
                        return;
                    }
                    BlockPos pos = BlockPos.containing(event.getRayTraceResult().getLocation());
                    if (SunriseGardening.CONFIG.splashWaterArea.get() == 0) {
                        if (level.getBlockState(pos).is(DewDropBlockTags.WATERABLE)) {
                            Util.setMoist(((ServerLevel) level), pos);
                        }
                    } else {
                        BlockPos.withinManhattanStream(pos, SunriseGardening.CONFIG.splashWaterArea.get(), 0, SunriseGardening.CONFIG.splashWaterArea.get()).forEach(blockPos -> {
                            BlockState state = level.getBlockState(blockPos);
                            if (state.is(DewDropBlockTags.WATERABLE)) {
                                Util.setMoist(((ServerLevel) level), blockPos);
                            } else if (level.getBlockState(blockPos.below()).is(DewDropBlockTags.WATERABLE)) {
                                Util.setMoist(((ServerLevel) level), blockPos.below());
                            }
                        });
                    }
                }
            }
        }

    }

    @SubscribeEvent
    public static void interactBlockEvent(PlayerInteractEvent.RightClickBlock event) {
        if (event.getLevel().isClientSide) {
            return;
        }

        if (!SunriseGardening.CONFIG.bucketWatering.get() && !SunriseGardening.CONFIG.bottleWatering.get()) {
            return;
        }

        ServerLevel level = (ServerLevel) event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);
        if (!state.is(DewDropBlockTags.WATERABLE)) {
            if (state.is(BlockTags.CROPS) && level.getBlockState(pos.below()).is(DewDropBlockTags.WATERABLE)) {
                pos = pos.below();
            } else {
                return;
            }
        }
        Player player = event.getEntity();
        ItemStack stack = player.getItemInHand(event.getHand());


        if (SunriseGardening.CONFIG.bucketWatering.get() && stack.is(Items.WATER_BUCKET)) {
            if (!player.isCreative()) {
                player.setItemInHand(event.getHand(), new ItemStack(Items.BUCKET));
            }
        } else if (SunriseGardening.CONFIG.bottleWatering.get() && stack.is(Items.POTION)) {
            Optional<Holder<Potion>> contents = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).potion();
            Potion type = contents.map(Holder::value).orElseGet(Potions.WATER::value);
            if (type.getEffects().isEmpty() || type == Potions.WATER) {
                if (!player.isCreative()) {
                    player.setItemInHand(event.getHand(), new ItemStack(Items.GLASS_BOTTLE));
                }
            } else return;
        } else {
            return;
        }
        Util.setMoist(level, pos);

    }

//
//    @SubscribeEvent
//    public static void interactSandEvent(PlayerInteractEvent event) {
//
//        Level level = event.getLevel();
//        BlockPos pos = event.getPos();
//        BlockState state = level.getBlockState(pos);
//        Player player = event.getEntity();
//        ItemStack stack = player.getItemInHand(event.getHand());
//
//        if (state.is(Blocks.SAND) && stack.getItem() instanceof HoeItem) {
//            level.setBlockAndUpdate(pos, ModElements.TILLED_SAND.get().defaultBlockState());
//            level.playSound(player, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
//            player.swing(event.getHand());
//            if (event.getLevel().isClientSide) {
//                player.getItemInHand(event.getHand()).hurtAndBreak(1, player, (pPlayer) -> pPlayer.broadcastBreakEvent(event.getHand()));
//            }
//
//        }
//
//    }

}