package com.portfolio.tests.ui;

import com.portfolio.base.AutomationInTestingUiTestBase;
import com.portfolio.pages.ReservationPage;
import org.junit.jupiter.api.Test;

public class BookingCreateTest extends AutomationInTestingUiTestBase {

    @Test
    void shouldStartAReservationFromAvailableRooms() {
        var dates = bookingPage.nextAvailableDates(7, 2);

        bookingPage.gotoPage();
        bookingPage.searchAvailability(dates.checkin(), dates.checkout());
        bookingPage.expectBookNowLinks();
        ReservationPage reservationPage = bookingPage.openFirstRoomReservationPage();
        reservationPage.expectReservationPage();
    }
}
