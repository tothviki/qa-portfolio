package com.portfolio.pages.mobile;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

public final class MobileReservationPage extends MobilePage {
    private static final By RESERVATION_HEADING = By.xpath(
            "//*[self::h1 or self::h2 or self::h3][normalize-space()='Single' or normalize-space()='Double' or normalize-space()='Suite']"
    );
    private static final By BOOK_THIS_ROOM_HEADING = By.xpath(
            "//*[self::h1 or self::h2 or self::h3][normalize-space()='Book This Room']"
    );
    private static final By PRICE_SUMMARY = By.xpath("//*[contains(normalize-space(.), 'Price Summary')]");
    private static final By RESERVE_NOW_BUTTON = By.xpath("//button[normalize-space()='Reserve Now']");

    public MobileReservationPage(AndroidDriver driver) {
        super(driver);
    }

    public void expectReservationPage() {
        visible(RESERVATION_HEADING);
        visible(BOOK_THIS_ROOM_HEADING);
        visible(PRICE_SUMMARY);
        visible(RESERVE_NOW_BUTTON);
    }
}
