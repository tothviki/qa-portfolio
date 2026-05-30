package com.portfolio.base;

import com.portfolio.clients.BookingClient;
import com.portfolio.models.booking.Booking;
import com.portfolio.models.booking.CreateBookingPayload;
import com.portfolio.models.booking.UpdateBookingPayload;
import io.restassured.response.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookingApiTestBase extends AutomationInTestingBaseTest {

    protected Booking createTrackedBooking(CreateBookingPayload payload) {
        Response response = BookingClient.createBooking(payload);
        assertStatus(response, 201);

        Booking booking = response.as(Booking.class);
        trackBooking(booking.bookingid());
        return booking;
    }

    protected void assertBookingContract(Booking booking) {
        assertNotNull(booking.bookingid(), "bookingid should be present");
        assertTrue(booking.bookingid() > 0, "bookingid should be positive");
        assertNotNull(booking.roomid(), "roomid should be present");
        assertTrue(booking.roomid() > 0, "roomid should be positive");
        assertNotNull(booking.firstname(), "firstname should be present");
        assertNotNull(booking.lastname(), "lastname should be present");
        assertNotNull(booking.depositpaid(), "depositpaid should be present");
        assertNotNull(booking.bookingdates(), "bookingdates should be present");
        assertNotNull(booking.bookingdates().checkin(), "checkin should be present");
        assertNotNull(booking.bookingdates().checkout(), "checkout should be present");
    }

    protected void assertBookingMatchesPayload(Booking booking, CreateBookingPayload payload) {
        assertBookingContract(booking);
        assertEquals(payload.roomid(), booking.roomid());
        assertEquals(payload.firstname(), booking.firstname());
        assertEquals(payload.lastname(), booking.lastname());
        assertEquals(payload.depositpaid(), booking.depositpaid());
        assertEquals(payload.bookingdates().checkin(), booking.bookingdates().checkin());
        assertEquals(payload.bookingdates().checkout(), booking.bookingdates().checkout());
    }

    protected void assertBookingMatchesPayload(Booking booking, UpdateBookingPayload payload) {
        assertBookingContract(booking);
        assertEquals(payload.roomid(), booking.roomid());
        assertEquals(payload.firstname(), booking.firstname());
        assertEquals(payload.lastname(), booking.lastname());
        assertEquals(payload.depositpaid(), booking.depositpaid());
        assertEquals(payload.bookingdates().checkin(), booking.bookingdates().checkin());
        assertEquals(payload.bookingdates().checkout(), booking.bookingdates().checkout());
    }
}
