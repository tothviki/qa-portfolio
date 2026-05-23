package com.portfolio.models;

import lombok.Builder;

/**
 * POJO representing a Booking in the Restful-Booker API.
 * Uses Lombok to reduce boilerplate code.
 */
@Builder
public record Booking(
        Integer id,
        String firstname,
        String lastname,
        Integer totalprice,
        Boolean depositpaid,
        BookingDates bookingdates,
        String additionalneeds) {

    @Builder
    public record BookingDates(
            String checkin,
            String checkout
    ) {
    }
}
