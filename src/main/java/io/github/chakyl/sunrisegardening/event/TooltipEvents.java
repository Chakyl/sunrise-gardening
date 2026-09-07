package io.github.chakyl.sunrisegardening.event;

import io.github.chakyl.sunrisegardening.SunriseGardening;
import io.github.chakyl.sunrisegardening.registry.DataMapRegistry;
import io.github.chakyl.sunrisegardening.registry.SunriseTags;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.List;

@EventBusSubscriber(modid = SunriseGardening.MODID, value = Dist.CLIENT)
public class TooltipEvents {
    @SubscribeEvent
    public static void tooltipEvent(ItemTooltipEvent event) {
        List<Component> comp = event.getToolTip();
        ItemStack stack = event.getItemStack();

        if (stack.getItem() instanceof BlockItem blockItem) {
            int tooltipIndex = comp.size();
            Integer growTime = BuiltInRegistries.BLOCK.wrapAsHolder(blockItem.getBlock()).getData(DataMapRegistry.CROP_GROW_TIMES);
            if (growTime == null) return;
            if (Screen.hasShiftDown()) {
                Integer regrowTime = BuiltInRegistries.BLOCK.wrapAsHolder(blockItem.getBlock()).getData(DataMapRegistry.MULTI_HARVEST_CROPS);

                comp.add(tooltipIndex, Component.translatable("tooltip.sunrisegardening.harvest_info", growTime).withStyle(ChatFormatting.WHITE));
                tooltipIndex++;
                comp.add(tooltipIndex, Component.translatable("tooltip.sunrisegardening.grow_time", growTime).withStyle(ChatFormatting.GRAY));
                tooltipIndex++;
                if (!blockItem.getBlock().defaultBlockState().is(SunriseTags.SINGLE_HARVEST_CROP)) {
                    comp.add(tooltipIndex, Component.translatable("tooltip.sunrisegardening.regrow_time", regrowTime != null ? growTime - regrowTime : growTime).withStyle(ChatFormatting.GRAY));
                    tooltipIndex++;
                    comp.add(tooltipIndex, Component.translatable("tooltip.sunrisegardening.multi_harvest", growTime).withStyle(ChatFormatting.GREEN));
                } else {
                    comp.add(tooltipIndex, Component.translatable("tooltip.sunrisegardening.single_harvest", growTime).withStyle(ChatFormatting.RED));
                }

            } else {
                comp.add(tooltipIndex, Component.translatable("tooltip.sunrisegardening.harvest_info_hold").withStyle(ChatFormatting.DARK_GRAY));

            }
        }
    }
}