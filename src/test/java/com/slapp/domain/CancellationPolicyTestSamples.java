package com.slapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CancellationPolicyTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static CancellationPolicy getCancellationPolicySample1() {
        return new CancellationPolicy().id(1L).name("name1").hoursBeforeEvent(1).refundPercentage(1);
    }

    public static CancellationPolicy getCancellationPolicySample2() {
        return new CancellationPolicy().id(2L).name("name2").hoursBeforeEvent(2).refundPercentage(2);
    }

    public static CancellationPolicy getCancellationPolicyRandomSampleGenerator() {
        return new CancellationPolicy()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .hoursBeforeEvent(intCount.incrementAndGet())
            .refundPercentage(intCount.incrementAndGet());
    }
}
