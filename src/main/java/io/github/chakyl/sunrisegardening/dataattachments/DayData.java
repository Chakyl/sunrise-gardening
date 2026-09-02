package io.github.chakyl.sunrisegardening.dataattachments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class DayData {
    public long dayLastGrown;
    public int totalDaysGrown;

    public DayData(long dayLastGrown, int totalDaysGrown) {
        this.dayLastGrown = dayLastGrown;
        this.totalDaysGrown = totalDaysGrown;
    }

    public static final Codec<DayData> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
            Codec.LONG.fieldOf("day_last_grown").forGetter(dayData -> dayData.dayLastGrown),
            Codec.INT.fieldOf("total_days_grown").forGetter(dayData -> dayData.totalDaysGrown)
        ).apply(instance, DayData::new)
    );
}