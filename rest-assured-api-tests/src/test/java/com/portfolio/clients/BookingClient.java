package com.portfolio.clients;

import com.portfolio.models.CreateBookingPayload;
import com.portfolio.models.UpdateBookingPayload;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.Map;

public class BookingClient {

    private BookingClient() {
    }

    public static Response createBooking(CreateBookingPayload booking) {
        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(booking)
                .when()
                .post("/api/booking");
    }

    public static Response postBookingPayload(Map<String, Object> payload) {
        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/api/booking");
    }

    public static Response postRawBookingPayload(String body) {
        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/api/booking");
    }

    public static Response getBooking(int id, String authToken) {
        return RestAssured
                .given()
                .cookie("token", authToken)
                .when()
                .get("/api/booking/" + id);
    }

    public static Response getBookingsByRoom(int roomId, String authToken) {
        return RestAssured
                .given()
                .cookie("token", authToken)
                .queryParam("roomid", roomId)
                .when()
                .get("/api/booking");
    }

    public static Response getBookingsWithoutRoom(String authToken) {
        return RestAssured
                .given()
                .cookie("token", authToken)
                .when()
                .get("/api/booking");
    }

    public static Response getBookingsWithoutAuth(int roomId) {
        return RestAssured
                .given()
                .queryParam("roomid", roomId)
                .when()
                .get("/api/booking");
    }

    public static Response updateBooking(int id, String token, UpdateBookingPayload booking) {
        return RestAssured
                .given()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(booking)
                .when()
                .put("/api/booking/" + id);
    }

    public static Response patchBooking(int id, String token, Map<String, Object> payload) {
        return RestAssured
                .given()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .patch("/api/booking/" + id);
    }

    public static Response deleteBooking(int id, String token) {
        if (token == null || token.isBlank()) {
            return RestAssured
                    .given()
                    .when()
                    .delete("/api/booking/" + id);
        }

        return RestAssured
                .given()
                .cookie("token", token)
                .when()
                .delete("/api/booking/" + id);
    }

    public static Response healthCheck() {
        return RestAssured
                .given()
                .when()
                .get("/api/booking/actuator/health");
    }

    public static Response getRooms() {
        return RestAssured
                .given()
                .when()
                .get("/api/room");
    }

    public static Response getBranding() {
        return RestAssured
                .given()
                .when()
                .get("/api/branding");
    }

    public static Response getMessages() {
        return RestAssured
                .given()
                .when()
                .get("/api/message");
    }
}
