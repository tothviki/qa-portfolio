package com.portfolio.models.booking;

public record Booking(
        Integer bookingid,
        Integer roomid,
        String firstname,
        String lastname,
        Boolean depositpaid,
        BookingDates bookingdates) {
}
