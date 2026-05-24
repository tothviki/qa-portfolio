package com.portfolio.tests;

import com.portfolio.base.AutomationInTestingBaseTest;
import com.portfolio.clients.AuthClient;
import com.portfolio.clients.BookingClient;
import com.portfolio.models.AuthCredentials;
import com.portfolio.models.AuthTokenResponse;
import com.portfolio.models.Booking;
import com.portfolio.models.BookingListResponse;
import com.portfolio.models.BookingDates;
import com.portfolio.models.BookingUpdateResponse;
import com.portfolio.models.BrandingResponse;
import com.portfolio.models.CreateBookingPayload;
import com.portfolio.models.ErrorResponse;
import com.portfolio.models.HealthResponse;
import com.portfolio.models.MessagesResponse;
import com.portfolio.models.MethodNotAllowedResponse;
import com.portfolio.models.RoomsResponse;
import com.portfolio.models.UpdateBookingPayload;
import com.portfolio.models.ValidationErrorsResponse;
import com.portfolio.payloads.AutomationInTestingTestData;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class AutomationInTestingApiTests extends AutomationInTestingBaseTest {

    private Booking createTrackedBooking(CreateBookingPayload payload) {
        Response response = BookingClient.createBooking(payload);
        assertStatus(response, 201);

        Booking booking = response.as(Booking.class);
        trackBooking(booking.bookingid());
        return booking;
    }

    private void assertStatus(Response response, int expectedStatus) {
        Assert.assertEquals(response.statusCode(), expectedStatus, response.asString());
    }

    private void assertBookingContract(Booking booking) {
        Assert.assertNotNull(booking.bookingid(), "bookingid should be present");
        Assert.assertTrue(booking.bookingid() > 0, "bookingid should be positive");
        Assert.assertNotNull(booking.roomid(), "roomid should be present");
        Assert.assertTrue(booking.roomid() > 0, "roomid should be positive");
        Assert.assertNotNull(booking.firstname(), "firstname should be present");
        Assert.assertNotNull(booking.lastname(), "lastname should be present");
        Assert.assertNotNull(booking.depositpaid(), "depositpaid should be present");
        Assert.assertNotNull(booking.bookingdates(), "bookingdates should be present");
        Assert.assertNotNull(booking.bookingdates().checkin(), "checkin should be present");
        Assert.assertNotNull(booking.bookingdates().checkout(), "checkout should be present");
    }

    private void assertBookingMatchesPayload(Booking booking, CreateBookingPayload payload) {
        assertBookingContract(booking);
        Assert.assertEquals(booking.roomid(), payload.roomid());
        Assert.assertEquals(booking.firstname(), payload.firstname());
        Assert.assertEquals(booking.lastname(), payload.lastname());
        Assert.assertEquals(booking.depositpaid(), payload.depositpaid());
        Assert.assertEquals(booking.bookingdates().checkin(), payload.bookingdates().checkin());
        Assert.assertEquals(booking.bookingdates().checkout(), payload.bookingdates().checkout());
    }

    private void assertBookingMatchesPayload(Booking booking, UpdateBookingPayload payload) {
        assertBookingContract(booking);
        Assert.assertEquals(booking.roomid(), payload.roomid());
        Assert.assertEquals(booking.firstname(), payload.firstname());
        Assert.assertEquals(booking.lastname(), payload.lastname());
        Assert.assertEquals(booking.depositpaid(), payload.depositpaid());
        Assert.assertEquals(booking.bookingdates().checkin(), payload.bookingdates().checkin());
        Assert.assertEquals(booking.bookingdates().checkout(), payload.bookingdates().checkout());
    }

    @Test(groups = "smoke")
    public void shouldCreateTokenWithValidCredentials() {
        Response response = AuthClient.createToken(new AuthCredentials("admin", "password"));

        assertStatus(response, 200);
        AuthTokenResponse body = response.as(AuthTokenResponse.class);
        Assert.assertNotNull(body.token());
        Assert.assertFalse(body.token().isBlank());
    }

    @Test(groups = "regression")
    public void shouldRejectInvalidCredentials() {
        Response response = AuthClient.createToken(new AuthCredentials("admin", "wrong-password"));

        assertStatus(response, 401);
        ErrorResponse body = response.as(ErrorResponse.class);
        Assert.assertEquals(body.error(), "Invalid credentials");
    }

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
        Assert.assertNotNull(body.errors());
        Assert.assertFalse(body.errors().isEmpty());
    }

    @Test(groups = "regression")
    public void shouldRejectCheckoutBeforeCheckin() {
        Response response = BookingClient.postBookingPayload(AutomationInTestingTestData.checkoutBeforeCheckinPayload());

        assertStatus(response, 409);
        ErrorResponse body = response.as(ErrorResponse.class);
        Assert.assertEquals(body.error(), "Failed to create booking");
    }

    @Test(groups = "regression")
    public void shouldRejectEachMissingRequiredGuestNameFieldFirstname() {
        Response response = BookingClient.postBookingPayload(AutomationInTestingTestData.missingFirstnamePayload());

        assertStatus(response, 400);
        ValidationErrorsResponse body = response.as(ValidationErrorsResponse.class);
        Assert.assertNotNull(body.errors());
        Assert.assertFalse(body.errors().isEmpty());
    }

    @Test(groups = "regression")
    public void shouldRejectEachMissingRequiredGuestNameFieldLastname() {
        Response response = BookingClient.postBookingPayload(AutomationInTestingTestData.missingLastnamePayload());

        assertStatus(response, 400);
        ValidationErrorsResponse body = response.as(ValidationErrorsResponse.class);
        Assert.assertNotNull(body.errors());
        Assert.assertFalse(body.errors().isEmpty());
    }

    @Test(groups = "regression")
    public void shouldRejectMalformedJson() {
        Response response = BookingClient.postRawBookingPayload(AutomationInTestingTestData.malformedBookingPayload());

        Assert.assertTrue(
                response.statusCode() == 400 || response.statusCode() == 500,
                "Expected 400 or 500, but received " + response.statusCode() + ": " + response.asString()
        );
    }

    @Test(groups = "smoke")
    public void shouldGetBookingByValidId() {
        CreateBookingPayload payload = AutomationInTestingTestData.randomBooking();
        Booking created = createTrackedBooking(payload);

        Response response = BookingClient.getBooking(created.bookingid(), authToken);

        assertStatus(response, 200);
        Booking booking = response.as(Booking.class);
        assertBookingMatchesPayload(booking, payload);
        Assert.assertEquals(booking.bookingid(), created.bookingid());
    }

    @Test(groups = "regression")
    public void shouldListBookingsByRoomId() {
        CreateBookingPayload payload = AutomationInTestingTestData.randomBooking();
        Booking created = createTrackedBooking(payload);

        Response response = BookingClient.getBookingsByRoom(payload.roomid(), authToken);

        assertStatus(response, 200);
        BookingListResponse body = response.as(BookingListResponse.class);
        Assert.assertNotNull(body.bookings());
        Assert.assertTrue(
                body.bookings().stream().anyMatch(booking -> booking.bookingid().equals(created.bookingid())),
                "Expected booking list to include booking " + created.bookingid()
        );
    }

    @Test(groups = "regression")
    public void shouldRequireAuthentication() {
        Response response = BookingClient.getBookingsWithoutAuth(1);

        assertStatus(response, 401);
        ErrorResponse body = response.as(ErrorResponse.class);
        Assert.assertEquals(body.error(), "Authentication required");
    }

    @Test(groups = "regression")
    public void shouldRequireRoomIdFilter() {
        Response response = BookingClient.getBookingsWithoutRoom(authToken);

        assertStatus(response, 400);
        ErrorResponse body = response.as(ErrorResponse.class);
        Assert.assertEquals(body.error(), "Room ID is required");
    }

    @Test(groups = "smoke")
    public void shouldFullyUpdateBooking() {
        CreateBookingPayload payload = new CreateBookingPayload(
                1,
                "TempFirst",
                "TempLast",
                true,
                "temp.first." + System.currentTimeMillis() + "@example.com",
                "07123456789",
                new BookingDates("2028-01-10", "2028-01-12")
        );
        Booking created = createTrackedBooking(payload);
        UpdateBookingPayload updatedPayload = AutomationInTestingTestData.updatedBooking(created.roomid());

        Response response = BookingClient.updateBooking(created.bookingid(), authToken, updatedPayload);

        assertStatus(response, 200);
        BookingUpdateResponse body = response.as(BookingUpdateResponse.class);
        Assert.assertEquals(body.bookingid(), created.bookingid());
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
        Assert.assertEquals(body.error(), "Method Not Allowed");
    }

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
        Assert.assertEquals(booking.roomid(), Integer.valueOf(9999));
    }

    @Test(groups = "smoke")
    public void shouldReturnHealthUp() {
        Response response = BookingClient.healthCheck();

        assertStatus(response, 200);
        HealthResponse body = response.as(HealthResponse.class);
        Assert.assertEquals(body.status(), "UP");
    }

    @Test(groups = "smoke")
    public void shouldReturnBookableRooms() {
        Response response = BookingClient.getRooms();

        assertStatus(response, 200);
        RoomsResponse body = response.as(RoomsResponse.class);
        Assert.assertNotNull(body.rooms());
        Assert.assertFalse(body.rooms().isEmpty());
        Assert.assertTrue(
                body.rooms().stream().anyMatch(room -> Boolean.TRUE.equals(room.accessible())),
                "Expected at least one accessible room"
        );
    }

    @Test(groups = "regression")
    public void shouldReturnHotelBranding() {
        Response response = BookingClient.getBranding();

        assertStatus(response, 200);
        BrandingResponse body = response.as(BrandingResponse.class);
        Assert.assertNotNull(body.name());
        Assert.assertFalse(body.name().isBlank());
    }

    @Test(groups = "regression")
    public void shouldReturnMessageSummaries() {
        Response response = BookingClient.getMessages();

        assertStatus(response, 200);
        MessagesResponse body = response.as(MessagesResponse.class);
        Assert.assertNotNull(body.messages());
        Assert.assertFalse(body.messages().isEmpty());
    }
}
