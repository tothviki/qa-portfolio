package com.portfolio.pages;

import com.portfolio.data.AutomationInTestingTestData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookingPage extends BasePage {
    private static final String HEADING_XPATH = "//*[self::h1 or self::h2 or self::h3 or self::h4 or self::h5 or self::h6][normalize-space()=%s]";
    private static final By AVAILABILITY_HEADING = By.xpath(
            String.format(HEADING_XPATH, "'Check Availability & Book Your Stay'")
    );
    private static final By ROOM_LIST_HEADING = By.xpath(
            String.format(HEADING_XPATH, "'Our Rooms'")
    );
    private static final By BOOKING_SECTION = By.id("booking");
    private static final By CHECK_AVAILABILITY_BUTTON = By.xpath("//button[normalize-space()='Check Availability']");
    private static final By BOOK_NOW_LINKS = By.cssSelector("a[href*='/reservation/']");

    public BookingPage(WebDriver driver, String baseUrl) {
        super(driver, baseUrl);
    }

    public void gotoPage() {
        open("/#booking");
        visible(AVAILABILITY_HEADING);
    }

    public void gotoRooms() {
        open("/#rooms");
        visible(ROOM_LIST_HEADING);
    }

    public void searchAvailability(String checkin, String checkout) {
        type(controlByLabel(BOOKING_SECTION, "Check In"), checkin);
        type(controlByLabel(BOOKING_SECTION, "Check Out"), checkout);
        click(CHECK_AVAILABILITY_BUTTON);
    }

    public void expectBookNowLinks() {
        assertFalse(bookNowLinks().isEmpty(), "At least one room should expose a Book now link");
    }

    public void expectBookNowLinksAtLeast(int minimumCount) {
        assertTrue(
                bookNowLinks().size() >= minimumCount,
                "Rooms section should expose at least " + minimumCount + " Book now links"
        );
    }

    public void expectRoomSummaries() {
        visible(ROOM_LIST_HEADING);
        expectBookNowLinksAtLeast(2);
    }

    public void openFirstRoomReservation() {
        click(BOOK_NOW_LINKS);
    }

    public ReservationPage openFirstRoomReservationPage() {
        openFirstRoomReservation();
        return new ReservationPage(driver, baseUrl());
    }

    public DateRange nextAvailableDates(int startOffset, int stayLength) {
        return new DateRange(
                AutomationInTestingTestData.isoDateFromToday(startOffset),
                AutomationInTestingTestData.isoDateFromToday(startOffset + stayLength)
        );
    }

    public List<WebElement> bookNowLinks() {
        return visibleElements(BOOK_NOW_LINKS);
    }

    public record DateRange(String checkin, String checkout) {
    }
}
