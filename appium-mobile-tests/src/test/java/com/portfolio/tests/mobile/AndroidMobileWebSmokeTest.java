package com.portfolio.tests.mobile;

import com.portfolio.base.AndroidMobileWebTestBase;
import com.portfolio.pages.mobile.MobileBookingPage;
import com.portfolio.pages.mobile.MobileContactPage;
import com.portfolio.pages.mobile.MobileHomePage;
import com.portfolio.pages.mobile.MobileReservationPage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AndroidMobileWebSmokeTest extends AndroidMobileWebTestBase {
    @Test
    void shouldOpenTheBookingSectionAndShowRoomsOnMobileChrome() {
        MobileHomePage homePage = new MobileHomePage(driver);
        homePage.openBookingSection(MOBILE_BASE_URL);
        homePage.expectBookingAndRoomsVisible();
    }

    @Test
    void shouldCompleteTheBookingFlowOnMobileChrome() {
        String checkin = MobileBookingPage.futureDate(7);
        String checkout = MobileBookingPage.futureDate(9);

        MobileBookingPage bookingPage = new MobileBookingPage(driver);
        MobileReservationPage reservationPage = new MobileReservationPage(driver);

        new MobileHomePage(driver).openBookingSection(MOBILE_BASE_URL);
        bookingPage.searchAvailability(checkin, checkout);

        String reservationHref = bookingPage.firstReservationHref();
        assertTrue(
                reservationHref.contains("checkin=" + checkin)
                        && reservationHref.contains("checkout=" + checkout),
                "Reservation link should carry the selected dates"
        );

        bookingPage.openFirstReservation();
        reservationPage.expectReservationPage();
    }

    @Test
    void shouldRejectAnInvalidContactSubmissionOnMobileChrome() {
        MobileContactPage contactPage = new MobileContactPage(driver);
        new MobileHomePage(driver).openContactSection(MOBILE_BASE_URL);
        contactPage.submitInvalidMessage();
        contactPage.expectValidationFeedback();
    }
}
