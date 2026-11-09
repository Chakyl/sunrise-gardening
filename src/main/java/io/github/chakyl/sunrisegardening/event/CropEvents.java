package io.github.chakyl.sunrisegardening.event;

import io.github.chakyl.sunrisegardening.SunriseGardening;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.BonemealEvent;

import static io.github.chakyl.sunrisegardening.util.CropHandlerUtils.getSoilPos;
import static io.github.chakyl.sunrisegardening.util.DayDataUtils.syncDayData;

@EventBusSubscriber(modid = SunriseGardening.MODID)
public class CropEvents {
    @SubscribeEvent
    public static void onBoneMeal(BonemealEvent event) {
        Level level = event.getLevel();
        if (level.isClientSide() || event.isCanceled()) return;
        syncDayData((ServerLevel) level, getSoilPos(level, event.getPos()), event.getState());
    }
}
