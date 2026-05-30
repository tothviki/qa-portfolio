package com.portfolio.base;

import com.portfolio.clients.AuthClient;
import com.portfolio.clients.BookingClient;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AutomationInTestingBaseTest {

    protected static final String BASE_URL = System.getenv().getOrDefault(
            "AUTOMATION_IN_TESTING_BASE_URL",
            "https://automationintesting.online"
    );
    protected static String authToken;
    private final Set<Integer> trackedBookingIds = new LinkedHashSet<>();

    @BeforeClass(alwaysRun = true)
    public void setup() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        authToken = AuthClient.createToken();
    }

    @AfterMethod(alwaysRun = true)
    public void cleanupTrackedBookings() {
        if (trackedBookingIds.isEmpty() || authToken == null || authToken.isBlank()) {
            return;
        }

        for (Integer bookingId : new ArrayList<>(trackedBookingIds)) {
            Response response = BookingClient.deleteBooking(bookingId, authToken);
            assertEquals(
                    202,
                    response.statusCode(),
                    "Failed to delete tracked booking " + bookingId + ": " + response.asString()
            );
            trackedBookingIds.remove(bookingId);
        }
    }

    protected void trackBooking(int bookingId) {
        trackedBookingIds.add(bookingId);
    }

    protected void untrackBooking(int bookingId) {
        trackedBookingIds.remove(bookingId);
    }

    protected void assertStatus(Response response, int expectedStatus) {
        assertEquals(expectedStatus, response.statusCode(), response.asString());
    }
}
