package com.portfolio.clients;

import com.portfolio.models.AuthCredentials;
import com.portfolio.models.AuthTokenResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class AuthClient {

    private AuthClient() {
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
        return createToken(new AuthCredentials("admin", "password"))
                .then()
                .statusCode(200)
                .extract()
                .as(AuthTokenResponse.class)
                .token();
    }

    public static String createToken(String username, String password) {
        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(new AuthCredentials(username, password))
                .when()
                .post("/api/auth/login")
                .then()
                .extract()
                .as(AuthTokenResponse.class)
                .token();
    }
}
