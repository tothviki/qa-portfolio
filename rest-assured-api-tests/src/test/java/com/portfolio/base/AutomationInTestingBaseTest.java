package com.portfolio.base;

import com.portfolio.clients.AuthClient;
import com.portfolio.clients.BookingClient;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterMethod;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

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
            Assert.assertEquals(
                    response.statusCode(),
                    202,
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
}
