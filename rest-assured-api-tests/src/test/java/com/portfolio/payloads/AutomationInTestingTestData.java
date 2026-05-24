package com.portfolio.payloads;

import com.portfolio.models.booking.BookingDates;
import com.portfolio.models.booking.CreateBookingPayload;
import com.portfolio.models.booking.UpdateBookingPayload;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class AutomationInTestingTestData {
    private static final String RUN_ID = System.getenv().getOrDefault("TEST_RUN_ID", Long.toString(System.currentTimeMillis(), 36));
    private static final AtomicInteger SEQUENCE = new AtomicInteger();

    private AutomationInTestingTestData() {
        throw new IllegalStateException("Utility class");
    }

    private static String nextId(String prefix) {
        return prefix + "-" + RUN_ID + "-" + String.format("%02d", SEQUENCE.incrementAndGet());
    }

    private static String isoDateFromToday(int offsetDays) {
        LocalDate today = LocalDate.now(ZoneOffset.UTC);
        return today.plusDays(offsetDays).toString();
    }

    private static BookingDates futureDates(int startDayOffset, int stayLength) {
        return new BookingDates(
                isoDateFromToday(startDayOffset),
                isoDateFromToday(startDayOffset + stayLength)
        );
    }

    public static CreateBookingPayload randomBooking() {
        return randomBooking(1);
    }

    public static CreateBookingPayload randomBooking(int roomId) {
        String id = nextId("pw");
        String shortId = RUN_ID.substring(Math.max(0, RUN_ID.length() - 4)) + String.format("%02d", SEQUENCE.incrementAndGet());
        return new CreateBookingPayload(
                roomId,
                "F" + shortId,
                "L" + shortId,
                SEQUENCE.incrementAndGet() % 2 == 0,
                id + "@example.com",
                "07" + String.format("%02d", SEQUENCE.incrementAndGet()).repeat(5).substring(0, 9),
                futureDates(SEQUENCE.incrementAndGet() + 1, 2)
        );
    }

    public static CreateBookingPayload bookingWithSupportedCharacters() {
        return bookingWithSupportedCharacters(1);
    }

    public static CreateBookingPayload bookingWithSupportedCharacters(int roomId) {
        return new CreateBookingPayload(
                roomId,
                "Anne-Marie",
                "OConnor-Smith",
                true,
                "anne.marie-" + nextId("chars") + "@example.com",
                "07123456789",
                futureDates(2, 2)
        );
    }

    public static UpdateBookingPayload updatedBooking(int roomId) {
        return new UpdateBookingPayload(
                roomId,
                "UpdatedFirst",
                "UpdatedLast",
                false,
                futureDates(6, 2)
        );
    }

    public static CreateBookingPayload temporaryBooking(int roomId) {
        return new CreateBookingPayload(
                roomId,
                "TempFirst",
                "TempLast",
                true,
                "temp.first." + nextId("update") + "@example.com",
                "07123456789",
                futureDates(4, 2)
        );
    }

    public static Map<String, Object> invalidBookingPayload() {
        return bookingPayload(
                1,
                "Al",
                "",
                true,
                "not-an-email",
                "123",
                dateMap("", "")
        );
    }

    public static Map<String, Object> missingFirstnamePayload() {
        Map<String, Object> payload = validBookingPayload();
        payload.remove("firstname");
        return payload;
    }

    public static Map<String, Object> missingLastnamePayload() {
        Map<String, Object> payload = validBookingPayload();
        payload.remove("lastname");
        return payload;
    }

    public static Map<String, Object> checkoutBeforeCheckinPayload() {
        return bookingPayload(
                1,
                "Edge",
                "Case",
                true,
                "edge@example.com",
                "07123456789",
                dateMap(isoDateFromToday(8), isoDateFromToday(6))
        );
    }

    public static String malformedBookingPayload() {
        return "{roomid:";
    }

    private static Map<String, Object> validBookingPayload() {
        return bookingPayload(
                1,
                "Edge",
                "Case",
                true,
                "edge@example.com",
                "07123456789",
                futureDateMap(5, 2)
        );
    }

    private static Map<String, Object> bookingPayload(
            int roomId,
            String firstname,
            String lastname,
            boolean depositPaid,
            String email,
            String phone,
            Map<String, Object> bookingDates
    ) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("roomid", roomId);
        payload.put("firstname", firstname);
        payload.put("lastname", lastname);
        payload.put("depositpaid", depositPaid);
        payload.put("email", email);
        payload.put("phone", phone);
        payload.put("bookingdates", bookingDates);
        return payload;
    }

    private static Map<String, Object> futureDateMap(int startDayOffset, int stayLength) {
        return dateMap(
                isoDateFromToday(startDayOffset),
                isoDateFromToday(startDayOffset + stayLength)
        );
    }

    private static Map<String, Object> dateMap(String checkin, String checkout) {
        Map<String, Object> bookingDates = new LinkedHashMap<>();
        bookingDates.put("checkin", checkin);
        bookingDates.put("checkout", checkout);
        return bookingDates;
    }
}
