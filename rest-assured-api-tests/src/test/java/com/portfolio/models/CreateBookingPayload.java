package com.portfolio.models;

public record CreateBookingPayload(
        Integer roomid,
        String firstname,
        String lastname,
        Boolean depositpaid,
        String email,
        String phone,
        BookingDates bookingdates
) {
}
