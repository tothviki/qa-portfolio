package com.portfolio.clients;

import com.portfolio.models.Booking;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class BookingClient {

    public static int createBooking(Booking booking) {
        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(booking)
                .when()
                .post("/booking")
                .then()
                .statusCode(200)
                .extract().path("bookingid");
    }

    public static Booking getBooking(int id) {
        return RestAssured
                .given()
                .when()
                .get("/booking/" + id)
                .then()
                .statusCode(200)
                .extract().as(Booking.class);
    }

    public static Booking updateBooking(int id, String token, Booking booking) {
        return RestAssured
                .given()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(booking)
                .when()
                .put("/booking/" + id)
                .then()
                .statusCode(200)
                .extract().as(Booking.class);
    }

    public static void deleteBooking(int id, String token) {
        RestAssured
                .given()
                .cookie("token", token)
                .when()
                .delete("/booking/" + id)
                .then()
                .statusCode(201);
    }
}
