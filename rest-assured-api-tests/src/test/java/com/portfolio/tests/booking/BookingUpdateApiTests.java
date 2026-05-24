package com.portfolio.tests.booking;

import com.portfolio.base.BookingApiTestBase;
import com.portfolio.clients.BookingClient;
import com.portfolio.models.booking.Booking;
import com.portfolio.models.booking.BookingUpdateResponse;
import com.portfolio.models.booking.UpdateBookingPayload;
import com.portfolio.models.common.MethodNotAllowedResponse;
import com.portfolio.payloads.AutomationInTestingTestData;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookingUpdateApiTests extends BookingApiTestBase {

    @Test(groups = "smoke")
    public void shouldFullyUpdateBooking() {
        Booking created = createTrackedBooking(AutomationInTestingTestData.temporaryBooking(1));
        UpdateBookingPayload updatedPayload = AutomationInTestingTestData.updatedBooking(created.roomid());

        Response response = BookingClient.updateBooking(created.bookingid(), authToken, updatedPayload);

        assertStatus(response, 200);
        BookingUpdateResponse body = response.as(BookingUpdateResponse.class);
        assertEquals(body.bookingid(), created.bookingid());
        assertBookingMatchesPayload(body.booking(), updatedPayload);
    }

    @Test(groups = "regression")
    public void shouldDocumentUnsupportedPatchMethod() {
        Booking created = createTrackedBooking(AutomationInTestingTestData.randomBooking());

        Response response = BookingClient.patchBooking(
                created.bookingid(),
                authToken,
                Map.of("firstname", "PatchedFirst")
        );

        assertStatus(response, 405);
        MethodNotAllowedResponse body = response.as(MethodNotAllowedResponse.class);
        assertEquals("Method Not Allowed", body.error());
    }
}
