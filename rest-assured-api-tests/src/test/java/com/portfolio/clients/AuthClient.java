package com.portfolio.clients;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class AuthClient {

    public static String createToken() {
        String payload = """
                {
                    "username": "admin",
                    "password": "password123"
                }
                """;

        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/auth")
                .then()
                .statusCode(200)
                .extract().path("token");
    }
}
