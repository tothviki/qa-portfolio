package com.portfolio.tests.ui;

import com.portfolio.base.AutomationInTestingUiTestBase;
import org.junit.jupiter.api.Test;

public class BookingAvailabilityTest extends AutomationInTestingUiTestBase {

    @Test
    void shouldCheckAvailabilityOfRooms() {
        var dates = bookingPage.nextAvailableDates(1, 1);

        bookingPage.gotoPage();
        bookingPage.searchAvailability(dates.checkin(), dates.checkout());
        bookingPage.expectBookNowLinks();
    }
}
