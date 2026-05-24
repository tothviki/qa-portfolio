package com.portfolio.tests.ui;

import com.portfolio.base.AutomationInTestingUiTestBase;
import com.portfolio.pages.ReservationPage;
import org.junit.jupiter.api.Test;

public class RoomsDetailsTest extends AutomationInTestingUiTestBase {

    @Test
    void shouldViewHotelRoomSummariesAndRoomDetails() {
        bookingPage.gotoRooms();
        bookingPage.expectRoomSummaries();
        bookingPage.expectBookNowLinksAtLeast(2);
        ReservationPage reservationPage = bookingPage.openFirstRoomReservationPage();
        reservationPage.expectRoomDetails();
        reservationPage.expectReservationPage();
    }
}
