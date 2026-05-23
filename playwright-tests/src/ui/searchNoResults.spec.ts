import { test } from '@playwright/test';
import { BookingPage } from '../pages/BookingPage';

test('UI - availability search returns a specific room result set @ui', async ({ page }) => {
  const bookingPage = new BookingPage(page);
  const { checkin, checkout } = bookingPage.nextAvailableDates(4, 1);

  await bookingPage.goto();
  await bookingPage.searchAvailability(checkin, checkout);
  await bookingPage.expectBookNowLinks(3);
  await bookingPage.expectRoomSummaries();
});
