package com.portfolio.base;

import com.portfolio.clients.AuthClient;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;

public class BaseTest {

    protected static final String BASE_URL = "https://restful-booker.herokuapp.com";
    protected static String authToken;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        authToken = AuthClient.createToken();
    }
}
