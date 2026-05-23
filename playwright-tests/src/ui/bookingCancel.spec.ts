import { test } from '@playwright/test';
import { BookingPage } from '../pages/BookingPage';

test('UI - reservation can be cancelled before guest details are submitted @ui', async ({ page }) => {
  const bookingPage = new BookingPage(page);
  const { checkin, checkout } = bookingPage.nextAvailableDates(10, 2);

  await test.step('Open a room reservation page', async () => {
    await bookingPage.goto();
    await bookingPage.searchAvailability(checkin, checkout);
    await bookingPage.openFirstRoomReservation();
  });

  await test.step('Start and cancel the reservation interaction', async () => {
    await bookingPage.startReservation();
    await bookingPage.cancelStartedReservation();
  });
});
