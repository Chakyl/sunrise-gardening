package io.github.chakyl.sunrisegardening.util;

import cool.bot.botslib.tag.DewDropBlockTags;
import io.github.chakyl.sunrisegardening.SunriseGardening;
import io.github.chakyl.sunrisegardening.blocks.GardenPotBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import sereneseasons.init.ModConfig;
import sereneseasons.init.ModFertility;
import sereneseasons.init.ModTags;

public class SeasonUtils {

    public static boolean isGlassAboveBlock(ServerLevel level, BlockPos cropPos) {
        BlockState scannedBlock;
        for (int i = 0; i < 16; ++i) {
            scannedBlock = level.getBlockState(cropPos.offset(0, i + 1, 0));
            if (SunriseGardening.CONFIG.strictGreenhouses.get() && scannedBlock.is(DewDropBlockTags.WATERABLE))
                return false;
            if (scannedBlock.is(ModTags.Blocks.GREENHOUSE_GLASS)) {
                return true;
            }
        }
        return false;
    }

    public static boolean canGrowInSeason(ServerLevel level, BlockState state, BlockState cropBlockState, BlockPos cropPos) {
        if (SunriseGardening.SERENE_SEASONS_INSTALLED) {
            ResourceLocation fertileKey = BuiltInRegistries.BLOCK.getKey(cropBlockState.getBlock());
            boolean fertile = ModFertility.isCropFertile(fertileKey.toString(), level, cropPos);
            return (!ModConfig.fertility.seasonalCrops || fertile || isGlassAboveBlock(level, cropPos) || ((state.getBlock() instanceof GardenPotBlock) && !level.canSeeSky(cropPos.above())));
        }
        return false;
    }

}