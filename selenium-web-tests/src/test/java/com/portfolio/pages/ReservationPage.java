package com.portfolio.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReservationPage extends BasePage {
    private static final String HEADING_XPATH = "//*[self::h1 or self::h2 or self::h3 or self::h4 or self::h5 or self::h6][normalize-space()=%s]";
    private static final By ROOM_HEADING = By.xpath(
            "//*[self::h1 or self::h2 or self::h3 or self::h4 or self::h5 or self::h6][normalize-space()='Single' or normalize-space()='Double' or normalize-space()='Suite']"
    );
    private static final By BOOK_THIS_ROOM_HEADING = By.xpath(
            String.format(HEADING_XPATH, "'Book This Room'")
    );
    private static final By PRICE_SUMMARY_TEXT = By.xpath("//*[contains(normalize-space(.), 'Price Summary')]");
    private static final By RESERVE_NOW_BUTTON = By.xpath("//button[normalize-space()='Reserve Now']");
    private static final By CANCEL_BUTTON = By.xpath("//button[normalize-space()='Cancel']");
    private static final By ROOM_DESCRIPTION = By.xpath(String.format(HEADING_XPATH, "'Room Description'"));
    private static final By ROOM_FEATURES = By.xpath(String.format(HEADING_XPATH, "'Room Features'"));
    private static final By ROOM_POLICIES = By.xpath(String.format(HEADING_XPATH, "'Room Policies'"));

    public ReservationPage(WebDriver driver, String baseUrl) {
        super(driver, baseUrl);
    }

    public void expectReservationPage() {
        visible(ROOM_HEADING);
        visible(BOOK_THIS_ROOM_HEADING);
        visible(PRICE_SUMMARY_TEXT);
        visible(RESERVE_NOW_BUTTON);
    }

    public void expectRoomDetails() {
        visible(ROOM_DESCRIPTION);
        visible(ROOM_FEATURES);
        visible(ROOM_POLICIES);
    }

    public void startReservation() {
        click(RESERVE_NOW_BUTTON);
    }

    public void cancelStartedReservation() {
        click(CANCEL_BUTTON);
        visible(RESERVE_NOW_BUTTON);
        assertTrue(driver.findElements(CANCEL_BUTTON).isEmpty(), "Cancel button should disappear after cancellation");
    }
}
