package com.portfolio.pages.mobile;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

import java.time.LocalDate;
import java.time.ZoneOffset;

public final class MobileBookingPage extends MobilePage {
    private static final By BOOKING_SECTION = By.id("booking");
    private static final By CHECK_IN_INPUT = By.xpath(".//label[normalize-space()='Check In']/following::input[1]");
    private static final By CHECK_OUT_INPUT = By.xpath(".//label[normalize-space()='Check Out']/following::input[1]");
    private static final By CHECK_AVAILABILITY_BUTTON = By.xpath("//button[normalize-space()='Check Availability']");
    private static final By BOOK_NOW_LINKS = By.cssSelector("#rooms a[href^='/reservation/']");

    public MobileBookingPage(AndroidDriver driver) {
        super(driver);
    }

    public void searchAvailability(String checkin, String checkout) {
        var bookingSection = visible(BOOKING_SECTION);
        bookingSection.findElement(CHECK_IN_INPUT).sendKeys(checkin);
        bookingSection.findElement(CHECK_OUT_INPUT).sendKeys(checkout);
        click(CHECK_AVAILABILITY_BUTTON);
        visible(BOOK_NOW_LINKS);
    }

    public String firstReservationHref() {
        return driver.findElements(BOOK_NOW_LINKS).getFirst().getAttribute("href");
    }

    public void openFirstReservation() {
        driver.findElements(BOOK_NOW_LINKS).getFirst().click();
    }

    public static String futureDate(int offsetDays) {
        return LocalDate.now(ZoneOffset.UTC).plusDays(offsetDays).toString();
    }
}
