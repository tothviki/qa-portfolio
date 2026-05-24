package com.portfolio.pages.mobile;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

public final class MobileHomePage extends MobilePage {
    private static final By BOOKING_HEADING = By.xpath(
            "//*[self::h1 or self::h2 or self::h3][normalize-space()='Check Availability & Book Your Stay']"
    );
    private static final By ROOMS_HEADING = By.xpath(
            "//*[self::h1 or self::h2 or self::h3][normalize-space()='Our Rooms']"
    );
    private static final By CONTACT_HEADING = By.xpath(
            "//*[self::h1 or self::h2 or self::h3][normalize-space()='Send Us a Message']"
    );

    public MobileHomePage(AndroidDriver driver) {
        super(driver);
    }

    public void openBookingSection(String url) {
        driver.get(url + "/#booking");
        visible(BOOKING_HEADING);
    }

    public void openContactSection(String url) {
        driver.get(url + "/#contact");
        visible(CONTACT_HEADING);
    }

    public void expectBookingAndRoomsVisible() {
        visible(BOOKING_HEADING);
        visible(ROOMS_HEADING);
    }
}
