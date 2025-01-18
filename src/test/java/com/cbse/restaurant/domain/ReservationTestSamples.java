package com.cbse.restaurant.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ReservationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Reservation getReservationSample1() {
        return new Reservation()
            .id(1L)
            .partySize(1)
            .customerName("customerName1")
            .customerEmail("customerEmail1")
            .customerPhone("customerPhone1")
            .specialRequests("specialRequests1");
    }

    public static Reservation getReservationSample2() {
        return new Reservation()
            .id(2L)
            .partySize(2)
            .customerName("customerName2")
            .customerEmail("customerEmail2")
            .customerPhone("customerPhone2")
            .specialRequests("specialRequests2");
    }

    public static Reservation getReservationRandomSampleGenerator() {
        return new Reservation()
            .id(longCount.incrementAndGet())
            .partySize(intCount.incrementAndGet())
            .customerName(UUID.randomUUID().toString())
            .customerEmail(UUID.randomUUID().toString())
            .customerPhone(UUID.randomUUID().toString())
            .specialRequests(UUID.randomUUID().toString());
    }
}
