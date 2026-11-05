package io.github.chakyl.sunrisegardening.client;

import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.chakyl.sunrisegardening.dataattachments.DayData;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

import java.util.Collections;
import java.util.Map;

@EventBusSubscriber(value = Dist.CLIENT)
public class FarmlandDebugRenderer {
    private static Map<BlockPos, DayData> chunkData = Collections.emptyMap();
    private static long renderExpiryTime = 0;
    private static long RENDER_DURATION = 60 * 20 * 60 * 1;

    public static void updateData(Map<BlockPos, DayData> newData) {
        chunkData = newData;
        Minecraft minecraft = Minecraft.getInstance();
        renderExpiryTime = minecraft.level.getGameTime() + RENDER_DURATION;
        minecraft.player.displayClientMessage(Component.translatable("messages.sunrisegardening.chunk_data_renderer.found", newData.size()).withStyle(ChatFormatting.GREEN), true);
    }

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.level.getGameTime() > renderExpiryTime || chunkData.isEmpty()) return;
            if (minecraft.player == null || minecraft.level == null) return;

            PoseStack poseStack = event.getPoseStack();
            ByteBufferBuilder byteBufferBuilder = new ByteBufferBuilder(256);
            MultiBufferSource.BufferSource bufferSource = MultiBufferSource.immediate(byteBufferBuilder);
            Vec3 cameraPos = minecraft.gameRenderer.getMainCamera().getPosition();

            for (Map.Entry<BlockPos, DayData> entry : chunkData.entrySet()) {
                BlockPos pos = entry.getKey();
                DayData data = entry.getValue();
                Component farmlandString = Component.translatable("messages.sunrisegardening.chunk_data_renderer.data", data.totalDaysGrown, data.dayLastGrown);

                poseStack.pushPose();
                poseStack.translate(pos.getX() - cameraPos.x + 0.5, pos.getY() - cameraPos.y + 1.2, pos.getZ() - cameraPos.z + 0.5);
                poseStack.mulPose(minecraft.getEntityRenderDispatcher().cameraOrientation());
                poseStack.scale(0.01f, -0.01f, 0.01f);
                minecraft.font.drawInBatch(
                        farmlandString,
                        -minecraft.font.width(farmlandString) / 2f,
                        0,
                        0xFFAA00,
                        true,
                        poseStack.last().pose(),
                        bufferSource,
                        Font.DisplayMode.SEE_THROUGH,
                        0,
                        15 // wtf is this supposed to be
                );
                poseStack.popPose();
            }

            bufferSource.endBatch();
            byteBufferBuilder.close();
        }
    }
}