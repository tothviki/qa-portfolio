package com.portfolio.tests;

import com.portfolio.base.AutomationInTestingBaseTest;
import com.portfolio.clients.AuthClient;
import com.portfolio.models.auth.AuthCredentials;
import com.portfolio.models.auth.AuthTokenResponse;
import com.portfolio.models.common.ErrorResponse;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AuthApiTests extends AutomationInTestingBaseTest {

    @Test(groups = "smoke")
    public void shouldCreateTokenWithValidCredentials() {
        Response response = AuthClient.createToken(new AuthCredentials("admin", "password"));

        assertStatus(response, 200);
        AuthTokenResponse body = response.as(AuthTokenResponse.class);
        assertNotNull(body.token());
        assertFalse(body.token().isBlank());
    }

    @Test(groups = "regression")
    public void shouldRejectInvalidCredentials() {
        Response response = AuthClient.createToken(new AuthCredentials("admin", "wrong-password"));

        assertStatus(response, 401);
        ErrorResponse body = response.as(ErrorResponse.class);
        assertEquals("Invalid credentials", body.error());
    }
}
