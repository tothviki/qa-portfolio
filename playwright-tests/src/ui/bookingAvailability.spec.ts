import { test } from '@playwright/test';
import { BookingPage } from '../pages/BookingPage';

test('UI - check availability of rooms @ui @smoke', async ({ page }) => {
  const bookingPage = new BookingPage(page);
  const { checkin, checkout } = bookingPage.nextAvailableDates(1, 1);

  await bookingPage.goto();
  await bookingPage.searchAvailability(checkin, checkout);
  await bookingPage.expectBookNowLinks();
});
