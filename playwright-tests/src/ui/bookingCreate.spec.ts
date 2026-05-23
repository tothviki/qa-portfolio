import { test } from '@playwright/test';
import { BookingPage } from '../pages/BookingPage';

test('UI - start a room reservation from available rooms @ui', async ({ page }) => {
  const bookingPage = new BookingPage(page);
  const { checkin, checkout } = bookingPage.nextAvailableDates(7, 2);

  await test.step('Search for available rooms', async () => {
    await bookingPage.goto();
    await bookingPage.searchAvailability(checkin, checkout);
  });

  await test.step('Open the first reservable room', async () => {
    await bookingPage.expectBookNowLinks();
    await bookingPage.expectFirstBookingLinkDates(checkin, checkout);
    await bookingPage.openFirstRoomReservation();
  });

  await test.step('Verify reservation page entry point', async () => {
    await bookingPage.expectReservationPage();
  });
});
