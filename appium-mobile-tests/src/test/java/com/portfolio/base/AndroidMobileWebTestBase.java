package com.portfolio.base;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public abstract class AndroidMobileWebTestBase {
    protected static final String APPIUM_SERVER_URL = System.getenv().getOrDefault(
            "APPIUM_SERVER_URL",
            "http://127.0.0.1:4723"
    );

    protected static final String ANDROID_DEVICE_NAME = System.getenv().getOrDefault(
            "ANDROID_DEVICE_NAME",
            "Android"
    );

    protected static final String ANDROID_UDID = System.getenv("ANDROID_UDID");
    protected static final String MOBILE_BASE_URL = System.getenv().getOrDefault(
            "MOBILE_BASE_URL",
            "https://automationintesting.online"
    );

    protected AndroidDriver driver;

    @BeforeEach
    void setUp() throws MalformedURLException {
        Assumptions.assumeTrue(
                Boolean.parseBoolean(System.getenv().getOrDefault("RUN_APPIUM_TESTS", "false")),
                "Set RUN_APPIUM_TESTS=true to run Appium tests."
        );

        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName("Android")
                .setAutomationName("UiAutomator2")
                .setDeviceName(ANDROID_DEVICE_NAME);
        options.setCapability("browserName", "Chrome");

        if (ANDROID_UDID != null && !ANDROID_UDID.isBlank()) {
            options.setUdid(ANDROID_UDID);
        }

        driver = new AndroidDriver(new URL(APPIUM_SERVER_URL), options);
        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
