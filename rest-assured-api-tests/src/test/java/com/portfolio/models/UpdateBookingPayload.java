package com.portfolio.models;

public record UpdateBookingPayload(
        Integer roomid,
        String firstname,
        String lastname,
        Boolean depositpaid,
        BookingDates bookingdates
) {
}
