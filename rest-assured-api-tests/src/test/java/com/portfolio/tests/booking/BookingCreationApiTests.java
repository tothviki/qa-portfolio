package com.portfolio.tests.booking;

import com.portfolio.base.BookingApiTestBase;
import com.portfolio.clients.BookingClient;
import com.portfolio.models.booking.Booking;
import com.portfolio.models.booking.CreateBookingPayload;
import com.portfolio.models.common.ErrorResponse;
import com.portfolio.models.common.ValidationErrorsResponse;
import com.portfolio.payloads.AutomationInTestingTestData;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookingCreationApiTests extends BookingApiTestBase {

    @Test(groups = "smoke")
    public void shouldCreateBookingWithValidData() {
        CreateBookingPayload payload = AutomationInTestingTestData.randomBooking();
        Booking booking = createTrackedBooking(payload);

        assertBookingMatchesPayload(booking, payload);
    }

    @Test(groups = "regression")
    public void shouldReturnValidationErrorsForInvalidPayload() {
        Response response = BookingClient.postBookingPayload(AutomationInTestingTestData.invalidBookingPayload());

        assertStatus(response, 400);
        ValidationErrorsResponse body = response.as(ValidationErrorsResponse.class);
        assertNotNull(body.errors());
        assertFalse(body.errors().isEmpty());
    }

    @Test(groups = "regression")
    public void shouldRejectCheckoutBeforeCheckin() {
        Response response = BookingClient.postBookingPayload(AutomationInTestingTestData.checkoutBeforeCheckinPayload());

        assertStatus(response, 409);
        ErrorResponse body = response.as(ErrorResponse.class);
        assertEquals("Failed to create booking", body.error());
    }

    @Test(groups = "regression")
    public void shouldRejectEachMissingRequiredGuestNameFieldFirstname() {
        Response response = BookingClient.postBookingPayload(AutomationInTestingTestData.missingFirstnamePayload());

        assertStatus(response, 400);
        ValidationErrorsResponse body = response.as(ValidationErrorsResponse.class);
        assertNotNull(body.errors());
        assertFalse(body.errors().isEmpty());
    }

    @Test(groups = "regression")
    public void shouldRejectEachMissingRequiredGuestNameFieldLastname() {
        Response response = BookingClient.postBookingPayload(AutomationInTestingTestData.missingLastnamePayload());

        assertStatus(response, 400);
        ValidationErrorsResponse body = response.as(ValidationErrorsResponse.class);
        assertNotNull(body.errors());
        assertFalse(body.errors().isEmpty());
    }

    @Test(groups = "regression")
    public void shouldRejectMalformedJson() {
        Response response = BookingClient.postRawBookingPayload(AutomationInTestingTestData.malformedBookingPayload());

        assertTrue(
                response.statusCode() == 400 || response.statusCode() == 500,
                "Expected 400 or 500, but received " + response.statusCode() + ": " + response.asString()
        );
    }

    @Test(groups = "regression")
    public void shouldAcceptSupportedPunctuationInNames() {
        CreateBookingPayload payload = AutomationInTestingTestData.bookingWithSupportedCharacters();
        Booking booking = createTrackedBooking(payload);

        assertBookingMatchesPayload(booking, payload);
    }

    @Test(groups = "regression")
    public void shouldAcceptUnknownRoomIds() {
        CreateBookingPayload payload = AutomationInTestingTestData.randomBooking(9999);
        Booking booking = createTrackedBooking(payload);

        assertBookingMatchesPayload(booking, payload);
        assertEquals(booking.roomid(), Integer.valueOf(9999));
    }
}
