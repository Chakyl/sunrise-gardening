package io.github.chakyl.sunrisegardening.event;

import cool.bot.botslib.tag.DewDropBlockTags;
import cool.bot.botslib.util.Util;
import io.github.chakyl.sunrisegardening.SunriseGardening;
import io.github.chakyl.sunrisegardening.registry.SunriseRegistry;
import io.github.chakyl.sunrisegardening.registry.SunriseTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

import java.util.Optional;

@EventBusSubscriber(modid = SunriseGardening.MODID)
public class FarmlandEvents {
    @SubscribeEvent
    public static void onTillDirtEvent(BlockEvent.BlockToolModificationEvent event) {
        ItemAbility ability = event.getItemAbility();
        // I can't figure out where tf hoe_till is coming from???
        if ((ability == ItemAbility.get("hoe_till") || ability == ItemAbilities.HOE_TILL) && event.getFinalState().is(SunriseTags.TURNS_INTO_TILLED_SOIL)) {
            event.setFinalState(SunriseRegistry.BlockRegistry.TILLED_SOIL.get().defaultBlockState());
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
            if (type.getEffects().isEmpty() ||type == Potions.WATER) {
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
                state = level.getBlockState(pos);
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