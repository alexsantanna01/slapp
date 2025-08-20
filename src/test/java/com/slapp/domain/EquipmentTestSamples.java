package com.slapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EquipmentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Equipment getEquipmentSample1() {
        return new Equipment().id(1L).name("name1").brand("brand1").model("model1");
    }

    public static Equipment getEquipmentSample2() {
        return new Equipment().id(2L).name("name2").brand("brand2").model("model2");
    }

    public static Equipment getEquipmentRandomSampleGenerator() {
        return new Equipment()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .brand(UUID.randomUUID().toString())
            .model(UUID.randomUUID().toString());
    }
}
