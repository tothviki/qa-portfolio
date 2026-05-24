import { test, expect } from '../fixtures/automationInTesting.fixture';
import { Booking } from '../models/automationInTesting';
import { BookingPage } from '../pages/BookingPage';
import { AutomationInTestingTestData } from '../utils/automationInTestingTestData';

type CreateBookingResponse = Booking;

test('E2E - API booking affects public room availability @e2e @smoke', async ({ page, automationApi }) => {
  const bookingPage = new BookingPage(page);
  const bookingData = AutomationInTestingTestData.randomBooking(1);

  await test.step('Create a booking for room 1 through the API', async () => {
    const createResponse = await automationApi.createBooking(bookingData);
    expect(createResponse.status()).toBe(201);

    const created = (await createResponse.json()) as CreateBookingResponse;
    automationApi.trackBooking(created.bookingid);
    expect(created.roomid).toBe(bookingData.roomid);
  });

  await test.step('Search the same dates on the public booking UI', async () => {
    await bookingPage.goto();
    await bookingPage.searchAvailability(bookingData.bookingdates.checkin, bookingData.bookingdates.checkout);
    await bookingPage.expectFirstBookingLinkDates(bookingData.bookingdates.checkin, bookingData.bookingdates.checkout);
  });

  await test.step('Verify the already booked room is unavailable', async () => {
    await bookingPage.expectBookedRoomUnavailable(bookingData.roomid);
  });
});
