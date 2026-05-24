package com.portfolio.pages;

import com.portfolio.data.AutomationInTestingTestData;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookingPage extends BasePage {
    private static final By AVAILABILITY_HEADING = By.xpath(
            "//*[self::h1 or self::h2 or self::h3][normalize-space()='Check Availability & Book Your Stay']"
    );
    private static final By ROOM_LIST_HEADING = By.xpath(
            "//*[self::h1 or self::h2 or self::h3][normalize-space()='Our Rooms']"
    );
    private static final By ROOMS_SECTION = By.id("rooms");
    private static final By BOOKING_SECTION = By.id("booking");
    private static final By CHECK_AVAILABILITY_BUTTON = By.xpath("//button[normalize-space()='Check Availability']");
    private static final By BOOK_NOW_LINKS = By.xpath(
            "//a[starts-with(@href, '/reservation/') and normalize-space()='Book now']"
    );

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

    public void expectRoomSummaries() {
        visible(ROOM_LIST_HEADING);
        String roomsText;
        try {
            roomsText = wait.until(unused -> {
                String candidate = driver.findElement(ROOMS_SECTION).getText();
                return candidate.contains("Single") && candidate.contains("Double")
                        ? candidate
                        : null;
            });
        } catch (TimeoutException exception) {
            dumpPageState("Timeout waiting for rooms summary text");
            throw exception;
        }
        assertTrue(roomsText.contains("Single"), "Rooms section should list the Single room");
        assertTrue(roomsText.contains("Double"), "Rooms section should list the Double room");
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
