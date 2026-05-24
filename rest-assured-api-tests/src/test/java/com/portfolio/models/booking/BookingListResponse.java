package com.portfolio.models.booking;

import java.util.List;

public record BookingListResponse(
        List<Booking> bookings
) {
}
