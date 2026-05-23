import { test } from '@playwright/test';
import { BookingPage } from '../pages/BookingPage';

test('UI - availability search updates room booking links with selected dates @ui', async ({ page }) => {
  const bookingPage = new BookingPage(page);
  const { checkin, checkout } = bookingPage.nextAvailableDates(2, 2);

  await bookingPage.goto();
  await bookingPage.searchAvailability(checkin, checkout);
  await bookingPage.expectBookNowLinks();
  await bookingPage.expectFirstBookingLinkDates(checkin, checkout);
});
