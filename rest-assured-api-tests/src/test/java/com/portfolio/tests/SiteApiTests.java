package com.portfolio.tests;

import com.portfolio.base.AutomationInTestingBaseTest;
import com.portfolio.clients.BookingClient;
import com.portfolio.models.site.BrandingResponse;
import com.portfolio.models.site.HealthResponse;
import com.portfolio.models.site.MessagesResponse;
import com.portfolio.models.site.RoomsResponse;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SiteApiTests extends AutomationInTestingBaseTest {

    @Test(groups = "smoke")
    public void shouldReturnHealthUp() {
        Response response = BookingClient.healthCheck();

        assertStatus(response, 200);
        HealthResponse body = response.as(HealthResponse.class);
        assertEquals("UP", body.status());
    }

    @Test(groups = "smoke")
    public void shouldReturnBookableRooms() {
        Response response = BookingClient.getRooms();

        assertStatus(response, 200);
        RoomsResponse body = response.as(RoomsResponse.class);
        assertNotNull(body.rooms());
        assertFalse(body.rooms().isEmpty());
        assertTrue(
                body.rooms().stream().anyMatch(room -> Boolean.TRUE.equals(room.accessible())),
                "Expected at least one accessible room"
        );
    }

    @Test(groups = "regression")
    public void shouldReturnHotelBranding() {
        Response response = BookingClient.getBranding();

        assertStatus(response, 200);
        BrandingResponse body = response.as(BrandingResponse.class);
        assertNotNull(body.name());
        assertFalse(body.name().isBlank());
    }

    @Test(groups = "regression")
    public void shouldReturnMessageSummaries() {
        Response response = BookingClient.getMessages();

        assertStatus(response, 200);
        MessagesResponse body = response.as(MessagesResponse.class);
        assertNotNull(body.messages());
        assertFalse(body.messages().isEmpty());
    }
}
