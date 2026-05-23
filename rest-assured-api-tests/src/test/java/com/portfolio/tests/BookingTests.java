package com.portfolio.tests;

import com.portfolio.base.BaseTest;
import com.portfolio.clients.BookingClient;
import com.portfolio.models.Booking;
import com.portfolio.payloads.BookingFactory;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class BookingTests extends BaseTest {

    private static int bookingId;

    @Test(priority = 1)
    public void createBooking() {
        Booking booking = BookingFactory.createBooking();

        bookingId = BookingClient.createBooking(booking);

        assertTrue(bookingId > 0);
    }

    @Test(priority = 2)
    public void getBooking() {

        Booking booking = BookingClient.getBooking(bookingId);

        assertEquals(booking.firstname(), "Test");
    }

    @Test(priority = 3)
    public void updateBooking() {

        Booking updated = BookingFactory.updateBooking();
        Booking response = BookingClient.updateBooking(bookingId, authToken, updated);

        assertEquals(response.lastname(), "Updated");
    }

    @Test(priority = 4)
    public void deleteBooking() {
        BookingClient.deleteBooking(bookingId, authToken);
    }
}
