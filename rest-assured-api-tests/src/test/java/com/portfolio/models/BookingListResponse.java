package com.portfolio.models;

import java.util.List;

public record BookingListResponse(
        List<Booking> bookings
) {
}
