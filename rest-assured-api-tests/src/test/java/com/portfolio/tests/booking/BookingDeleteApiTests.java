package com.portfolio.tests.booking;

import com.portfolio.base.BookingApiTestBase;
import com.portfolio.clients.BookingClient;
import com.portfolio.models.booking.Booking;
import com.portfolio.payloads.AutomationInTestingTestData;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class BookingDeleteApiTests extends BookingApiTestBase {

    @Test(groups = "regression")
    public void shouldRejectInvalidAuthToken() {
        Booking created = createTrackedBooking(AutomationInTestingTestData.randomBooking());

        Response response = BookingClient.deleteBooking(created.bookingid(), "not-a-real-token");

        assertStatus(response, 403);
    }

    @Test(groups = "smoke")
    public void shouldDeleteExistingBooking() {
        Booking created = createTrackedBooking(AutomationInTestingTestData.randomBooking());

        Response response = BookingClient.deleteBooking(created.bookingid(), authToken);

        assertStatus(response, 202);
        untrackBooking(created.bookingid());
    }
}
