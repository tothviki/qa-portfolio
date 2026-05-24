package com.portfolio.tests.ui;

import com.portfolio.base.AutomationInTestingUiTestBase;
import com.portfolio.pages.ReservationPage;
import org.junit.jupiter.api.Test;

public class BookingCancelTest extends AutomationInTestingUiTestBase {

    @Test
    void shouldCancelReservationBeforeGuestDetailsAreSubmitted() {
        var dates = bookingPage.nextAvailableDates(10, 2);

        bookingPage.gotoPage();
        bookingPage.searchAvailability(dates.checkin(), dates.checkout());
        ReservationPage reservationPage = bookingPage.openFirstRoomReservationPage();
        reservationPage.startReservation();
        reservationPage.cancelStartedReservation();
    }
}
