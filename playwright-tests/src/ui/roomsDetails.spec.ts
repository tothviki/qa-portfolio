import { test, expect } from '@playwright/test';
import { BookingPage } from '../pages/BookingPage';

test('UI - view hotel room summaries and room details @ui', async ({ page }) => {
  const bookingPage = new BookingPage(page);

  await test.step('Verify room summaries on the booking page', async () => {
    await page.goto('/#rooms');

    await bookingPage.expectRoomSummaries();
    await bookingPage.expectBookNowLinks();
  });

  await test.step('Open a room details page', async () => {
    await bookingPage.openFirstRoomReservation();

    await expect(page.getByRole('heading', { name: 'Room Description' })).toBeVisible();
    await expect(page.getByRole('heading', { name: 'Room Features' })).toBeVisible();
    await expect(page.getByRole('heading', { name: 'Room Policies' })).toBeVisible();
    await expect(page.getByRole('heading', { name: 'Book This Room' })).toBeVisible();
  });
});
