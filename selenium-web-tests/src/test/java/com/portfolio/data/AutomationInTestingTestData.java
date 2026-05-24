package com.portfolio.data;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.concurrent.atomic.AtomicInteger;

public final class AutomationInTestingTestData {
    private static final String RUN_ID = System.getenv().getOrDefault(
            "TEST_RUN_ID",
            Long.toString(System.currentTimeMillis(), 36)
    );
    private static final AtomicInteger SEQUENCE = new AtomicInteger();

    private AutomationInTestingTestData() {
        throw new IllegalStateException("Utility class");
    }

    public static String nextId(String prefix) {
        return prefix + "-" + RUN_ID + "-" + String.format("%02d", SEQUENCE.incrementAndGet());
    }

    public static String isoDateFromToday(int offsetDays) {
        LocalDate today = LocalDate.now(ZoneOffset.UTC);
        return today.plusDays(offsetDays).toString();
    }

    public static String randomName() {
        return "Test " + nextId("ui-name");
    }

    public static String randomEmail() {
        return nextId("ui-email") + "@example.com";
    }
}
