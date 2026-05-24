package com.portfolio.payloads;

import com.portfolio.models.BookingDates;
import com.portfolio.models.CreateBookingPayload;
import com.portfolio.models.UpdateBookingPayload;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class AutomationInTestingTestData {

    private static final String RUN_ID = System.getenv().getOrDefault("TEST_RUN_ID", Long.toString(System.currentTimeMillis(), 36));
    private static final AtomicInteger SEQUENCE = new AtomicInteger();

    private AutomationInTestingTestData() {
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

    public static UpdateBookingPayload updatedBooking() {
        return updatedBooking(1);
    }

    public static UpdateBookingPayload updatedBooking(int roomId) {
        return new UpdateBookingPayload(
                roomId,
                "UpdatedFirst",
                "UpdatedLast",
                false,
                new BookingDates("2028-01-14", "2028-01-16")
        );
    }

    public static CreateBookingPayload namedBooking(String firstname, String lastname) {
        return namedBooking(firstname, lastname, 1);
    }

    public static CreateBookingPayload namedBooking(String firstname, String lastname, int roomId) {
        return new CreateBookingPayload(
                roomId,
                firstname,
                lastname,
                true,
                firstname.toLowerCase() + "." + lastname.toLowerCase() + "." + nextId("named") + "@example.com",
                "07" + String.format("%02d", SEQUENCE.incrementAndGet()).repeat(5).substring(0, 9),
                futureDates(4, 2)
        );
    }

    public static Map<String, Object> invalidBookingPayload() {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("roomid", 1);
        payload.put("firstname", "Al");
        payload.put("lastname", "");
        payload.put("depositpaid", true);
        payload.put("email", "not-an-email");
        payload.put("phone", "123");
        Map<String, Object> bookingDates = new LinkedHashMap<>();
        bookingDates.put("checkin", "");
        bookingDates.put("checkout", "");
        payload.put("bookingdates", bookingDates);
        return payload;
    }

    public static Map<String, Object> missingFirstnamePayload() {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("roomid", 1);
        payload.put("lastname", "Case");
        payload.put("depositpaid", true);
        payload.put("email", "edge@example.com");
        payload.put("phone", "07123456789");
        payload.put("bookingdates", futureDateMap(5, 2));
        return payload;
    }

    public static Map<String, Object> missingLastnamePayload() {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("roomid", 1);
        payload.put("firstname", "Case");
        payload.put("depositpaid", true);
        payload.put("email", "edge@example.com");
        payload.put("phone", "07123456789");
        payload.put("bookingdates", futureDateMap(5, 2));
        return payload;
    }

    public static Map<String, Object> checkoutBeforeCheckinPayload() {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("roomid", 1);
        payload.put("firstname", "Edge");
        payload.put("lastname", "Case");
        payload.put("depositpaid", true);
        payload.put("email", "edge@example.com");
        payload.put("phone", "07123456789");
        Map<String, Object> bookingDates = new LinkedHashMap<>();
        bookingDates.put("checkin", "2028-01-12");
        bookingDates.put("checkout", "2028-01-10");
        payload.put("bookingdates", bookingDates);
        return payload;
    }

    public static String malformedBookingPayload() {
        return "{roomid:";
    }

    private static Map<String, Object> futureDateMap(int startDayOffset, int stayLength) {
        Map<String, Object> bookingDates = new LinkedHashMap<>();
        bookingDates.put("checkin", isoDateFromToday(startDayOffset));
        bookingDates.put("checkout", isoDateFromToday(startDayOffset + stayLength));
        return bookingDates;
    }
}
