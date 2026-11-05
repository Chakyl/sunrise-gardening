package io.github.chakyl.sunrisegardening.command;

import com.mojang.brigadier.CommandDispatcher;
import io.github.chakyl.sunrisegardening.dataattachments.TilledBlockChunkData;
import io.github.chakyl.sunrisegardening.network.ClientBoundTilledDataPacket;
import io.github.chakyl.sunrisegardening.registry.SunriseRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.neoforge.network.PacketDistributor;

public class DebugFarmlandDataCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("sunrisegardening")
                        .requires(source -> source.hasPermission(4))
                        .then(Commands.literal("debugfarmlanddata")
                                .executes(context -> {
                                    ServerPlayer player = context.getSource().getPlayerOrException();
                                    ChunkPos chunkPos = player.chunkPosition();
                                    LevelChunk chunk = player.serverLevel().getChunk(chunkPos.x, chunkPos.z);

                                    TilledBlockChunkData data = chunk.getData(SunriseRegistry.AttachmentRegistry.TILLED_BLOCK_CHUNK_DATA.get());
                                    if (data.dayDataMap.isEmpty()) {
                                        player.displayClientMessage(Component.translatable("messages.sunrisegardening.chunk_data_renderer.not_found").withStyle(ChatFormatting.RED), true);
                                        return 0;
                                    }
                                    PacketDistributor.sendToPlayer(player, new ClientBoundTilledDataPacket(data.dayDataMap));
                                    return 1;
                                })
                        )
        );
    }
}