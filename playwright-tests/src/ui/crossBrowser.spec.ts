import { test, Page } from '@playwright/test';
import { BookingPage } from '../pages/BookingPage';

test.describe('UI - Cross-browser booking smoke checks', () => {
  async function goToBookingPage(page: Page) {
    const bookingPage = new BookingPage(page);
    await bookingPage.goto();
    return bookingPage;
  }

  test('UI CB: booking page exposes availability controls @ui @cross-browser @smoke', async ({ page }) => {
    const bookingPage = await goToBookingPage(page);
    const { checkin, checkout } = bookingPage.nextAvailableDates(3, 1);
    await bookingPage.searchAvailability(checkin, checkout);
    await bookingPage.expectBookNowLinks();
  });

  test('UI CB: availability search returns rooms with booking links @ui @cross-browser', async ({ page }) => {
    const bookingPage = await goToBookingPage(page);
    const { checkin, checkout } = bookingPage.nextAvailableDates(5, 2);
    await bookingPage.searchAvailability(checkin, checkout);
    await bookingPage.expectBookNowLinks();
    await bookingPage.expectRoomSummaries();
  });

  test('UI CB: room cards expose user-visible details @ui @cross-browser', async ({ page }) => {
    const bookingPage = await goToBookingPage(page);
    await bookingPage.expectRoomSummaries();
    await bookingPage.expectPriceSummaries();
  });

  test('UI CB: reservation page opens from a room card @ui @cross-browser', async ({ page }) => {
    const bookingPage = await goToBookingPage(page);
    await bookingPage.openFirstRoomReservation();
  });
});
