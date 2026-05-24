package com.portfolio.clients;

import com.portfolio.models.auth.AuthCredentials;
import com.portfolio.models.auth.AuthTokenResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class AuthClient {
    private static final String TEST_DEFAULT_USERNAME = "admin";
    private static final String TEST_DEFAULT_PASSWORD = "password";

    private AuthClient() {
        throw new IllegalStateException("Utility class");
    }

    public static Response createToken(AuthCredentials credentials) {
        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(credentials)
                .when()
                .post("/api/auth/login");
    }

    public static String createToken() {
        return createToken(TEST_DEFAULT_USERNAME, TEST_DEFAULT_PASSWORD);
    }

    public static String createToken(String username, String password) {
        return createToken(new AuthCredentials(username, password))
                .then()
                .statusCode(200)
                .extract()
                .as(AuthTokenResponse.class)
                .token();
    }
}
