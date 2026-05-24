package com.portfolio.tests.booking;

import com.portfolio.base.BookingApiTestBase;
import com.portfolio.clients.BookingClient;
import com.portfolio.models.booking.Booking;
import com.portfolio.models.booking.BookingListResponse;
import com.portfolio.models.booking.CreateBookingPayload;
import com.portfolio.models.common.ErrorResponse;
import com.portfolio.payloads.AutomationInTestingTestData;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookingReadApiTests extends BookingApiTestBase {

    @Test(groups = "smoke")
    public void shouldGetBookingByValidId() {
        CreateBookingPayload payload = AutomationInTestingTestData.randomBooking();
        Booking created = createTrackedBooking(payload);

        Response response = BookingClient.getBooking(created.bookingid(), authToken);

        assertStatus(response, 200);
        Booking booking = response.as(Booking.class);
        assertBookingMatchesPayload(booking, payload);
        assertEquals(booking.bookingid(), created.bookingid());
    }

    @Test(groups = "regression")
    public void shouldListBookingsByRoomId() {
        CreateBookingPayload payload = AutomationInTestingTestData.randomBooking();
        Booking created = createTrackedBooking(payload);

        Response response = BookingClient.getBookingsByRoom(payload.roomid(), authToken);

        assertStatus(response, 200);
        BookingListResponse body = response.as(BookingListResponse.class);
        assertNotNull(body.bookings());
        assertTrue(
                body.bookings().stream().anyMatch(booking -> booking.bookingid().equals(created.bookingid())),
                "Expected booking list to include booking " + created.bookingid()
        );
    }

    @Test(groups = "regression")
    public void shouldRequireAuthentication() {
        Response response = BookingClient.getBookingsWithoutAuth(1);

        assertStatus(response, 401);
        ErrorResponse body = response.as(ErrorResponse.class);
        assertEquals("Authentication required", body.error());
    }

    @Test(groups = "regression")
    public void shouldRequireRoomIdFilter() {
        Response response = BookingClient.getBookingsWithoutRoom(authToken);

        assertStatus(response, 400);
        ErrorResponse body = response.as(ErrorResponse.class);
        assertEquals("Room ID is required", body.error());
    }
}
