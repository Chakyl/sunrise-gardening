package io.github.chakyl.sunrisegardening.network;

import io.github.chakyl.sunrisegardening.client.FarmlandDebugRenderer;
import io.github.chakyl.sunrisegardening.dataattachments.DayData;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.HashMap;
import java.util.Map;

import static io.github.chakyl.sunrisegardening.SunriseGardening.loc;

public record ClientBoundTilledDataPacket(Map<BlockPos, DayData> dataMap) implements CustomPacketPayload {
    public static final Type<ClientBoundTilledDataPacket> TYPE = new Type<>(loc("tilled_data_sync"));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientBoundTilledDataPacket> STREAM_CODEC = StreamCodec.of(
            (buf, packet) -> {
                buf.writeInt(packet.dataMap.size());
                packet.dataMap.forEach((pos, data) -> {
                    buf.writeBlockPos(pos);
                    buf.writeLong(data.dayLastGrown);
                    buf.writeInt(data.totalDaysGrown);
                });
            },
            buf -> {
                int size = buf.readInt();
                Map<BlockPos, DayData> map = new HashMap<>();
                for (int i = 0; i < size; i++) {
                    BlockPos pos = buf.readBlockPos();
                    long lastGrown = buf.readLong();
                    int totalGrown = buf.readInt();
                    map.put(pos, new DayData(lastGrown, totalGrown));
                }
                return new ClientBoundTilledDataPacket(map);
            }
    );

    public static void handle(ClientBoundTilledDataPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            FarmlandDebugRenderer.updateData(packet.dataMap());
        });
    }
}