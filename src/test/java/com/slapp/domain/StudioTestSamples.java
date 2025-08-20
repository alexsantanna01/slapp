package com.slapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class StudioTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Studio getStudioSample1() {
        return new Studio()
            .id(1L)
            .name("name1")
            .address("address1")
            .city("city1")
            .state("state1")
            .zipCode("zipCode1")
            .phone("phone1")
            .email("email1")
            .website("website1")
            .image("image1");
    }

    public static Studio getStudioSample2() {
        return new Studio()
            .id(2L)
            .name("name2")
            .address("address2")
            .city("city2")
            .state("state2")
            .zipCode("zipCode2")
            .phone("phone2")
            .email("email2")
            .website("website2")
            .image("image2");
    }

    public static Studio getStudioRandomSampleGenerator() {
        return new Studio()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .address(UUID.randomUUID().toString())
            .city(UUID.randomUUID().toString())
            .state(UUID.randomUUID().toString())
            .zipCode(UUID.randomUUID().toString())
            .phone(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .website(UUID.randomUUID().toString())
            .image(UUID.randomUUID().toString());
    }
}
