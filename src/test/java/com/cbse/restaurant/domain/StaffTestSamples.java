package com.cbse.restaurant.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class StaffTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Staff getStaffSample1() {
        return new Staff().id(1L).firstName("firstName1").lastName("lastName1").email("email1").phone("phone1");
    }

    public static Staff getStaffSample2() {
        return new Staff().id(2L).firstName("firstName2").lastName("lastName2").email("email2").phone("phone2");
    }

    public static Staff getStaffRandomSampleGenerator() {
        return new Staff()
            .id(longCount.incrementAndGet())
            .firstName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .phone(UUID.randomUUID().toString());
    }
}
