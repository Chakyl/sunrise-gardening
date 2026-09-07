package io.github.chakyl.sunrisegardening.dataattachments;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;

import java.util.HashMap;
import java.util.Map;

public class TilledBlockChunkData {
    public final Map<BlockPos, DayData> dayDataMap = new HashMap<>();

    public static final Codec<TilledBlockChunkData> CODEC = Codec.pair(BlockPos.CODEC.fieldOf("pos").codec(), DayData.CODEC.fieldOf("day_data").codec()).listOf().xmap(
            pairs -> {
                TilledBlockChunkData data = new TilledBlockChunkData();
                for (Pair<BlockPos, DayData> pair : pairs) {
                    data.dayDataMap.put(pair.getFirst(), pair.getSecond());
                }
                return data;
            },
            data -> data.dayDataMap.entrySet().stream().map(entry -> Pair.of(entry.getKey(), entry.getValue()))
                    .toList()
    );
}