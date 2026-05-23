package com.portfolio.payloads;

import com.portfolio.models.Booking;

public class BookingFactory {

    public static Booking createBooking() {
        return Booking.builder()
                .firstname("Test")
                .lastname("Tester")
                .totalprice(150)
                .depositpaid(true)
                .bookingdates(
                        Booking.BookingDates.builder()
                                .checkin("2024-01-01")
                                .checkout("2024-01-10")
                                .build()
                )
                .additionalneeds("Breakfast")
                .build();
    }

    public static Booking updateBooking() {
        return Booking.builder()
                .firstname("Test")
                .lastname("Updated")
                .totalprice(200)
                .depositpaid(false)
                .bookingdates(
                        Booking.BookingDates.builder()
                                .checkin("2024-02-01")
                                .checkout("2024-02-10")
                                .build()
                )
                .additionalneeds("Dinner")
                .build();
    }
}
