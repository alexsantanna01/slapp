package com.slapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class RoomImageTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static RoomImage getRoomImageSample1() {
        return new RoomImage().id(1L).url("url1").altText("altText1").displayOrder(1);
    }

    public static RoomImage getRoomImageSample2() {
        return new RoomImage().id(2L).url("url2").altText("altText2").displayOrder(2);
    }

    public static RoomImage getRoomImageRandomSampleGenerator() {
        return new RoomImage()
            .id(longCount.incrementAndGet())
            .url(UUID.randomUUID().toString())
            .altText(UUID.randomUUID().toString())
            .displayOrder(intCount.incrementAndGet());
    }
}
