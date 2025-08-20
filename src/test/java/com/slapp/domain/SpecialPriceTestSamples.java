package com.slapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SpecialPriceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SpecialPrice getSpecialPriceSample1() {
        return new SpecialPrice().id(1L).description("description1");
    }

    public static SpecialPrice getSpecialPriceSample2() {
        return new SpecialPrice().id(2L).description("description2");
    }

    public static SpecialPrice getSpecialPriceRandomSampleGenerator() {
        return new SpecialPrice().id(longCount.incrementAndGet()).description(UUID.randomUUID().toString());
    }
}
