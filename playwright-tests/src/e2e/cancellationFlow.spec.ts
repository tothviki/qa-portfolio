import { test, expect } from '../fixtures/automationInTesting.fixture';
import { Booking } from '../models/automationInTesting';
import { BookingPage } from '../pages/BookingPage';
import { AutomationInTestingTestData } from '../utils/automationInTestingTestData';

type CreateBookingResponse = Booking;

test('E2E - cancel a booking and restore room availability @e2e @regression', async ({ page, automationApi }) => {
  const bookingPage = new BookingPage(page);
  const bookingData = AutomationInTestingTestData.randomBooking(1);
  let bookingId = 0;

  await test.step('Create a booking through the API', async () => {
    const createResponse = await automationApi.createBooking(bookingData);
    expect(createResponse.status()).toBe(201);

    const created = (await createResponse.json()) as CreateBookingResponse;
    bookingId = created.bookingid;
    automationApi.trackBooking(created.bookingid);
    expect(created.roomid).toBe(bookingData.roomid);
  });

  await test.step('Verify the booked room is hidden in the public UI', async () => {
    await bookingPage.goto();
    await bookingPage.searchAvailability(bookingData.bookingdates.checkin, bookingData.bookingdates.checkout);
    await bookingPage.expectBookedRoomUnavailable(bookingData.roomid);
  });

  await test.step('Cancel the booking through the API', async () => {
    const authToken = await automationApi.authenticate();
    expect(bookingId).toBeGreaterThan(0);
    const deleteResponse = await automationApi.deleteBooking(bookingId, authToken);

    expect(deleteResponse.status()).toBe(202);
    automationApi.untrackBooking(bookingId);
  });

  await test.step('Verify the room becomes available again', async () => {
    await bookingPage.goto();
    await bookingPage.searchAvailability(bookingData.bookingdates.checkin, bookingData.bookingdates.checkout);
    await bookingPage.expectBookedRoomAvailable(bookingData.roomid);
  });
});
