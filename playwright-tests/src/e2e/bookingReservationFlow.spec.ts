import { test, expect } from '../fixtures/automationInTesting.fixture';
import { Booking } from '../models/automationInTesting';
import { BookingPage } from '../pages/BookingPage';
import { AutomationInTestingTestData } from '../utils/automationInTestingTestData';

test('E2E - complete a public booking and verify persisted booking state via API @e2e @smoke', async ({
  page,
  automationApi,
}) => {
  const bookingPage = new BookingPage(page);
  const bookingData = AutomationInTestingTestData.randomBooking(1);
  let createdBookingId = 0;

  await test.step('Search for an available room and open the reservation page', async () => {
    await bookingPage.goto();
    await bookingPage.searchAvailability(bookingData.bookingdates.checkin, bookingData.bookingdates.checkout);
    await bookingPage.expectBookNowLinks();
    await bookingPage.expectFirstBookingLinkDates(bookingData.bookingdates.checkin, bookingData.bookingdates.checkout);
    await bookingPage.openFirstRoomReservation();
  });

  await test.step('Complete the reservation in the public UI', async () => {
    const bookingResponsePromise = page.waitForResponse(
      (response) =>
        response.url().includes('/api/booking') &&
        response.request().method() === 'POST' &&
        response.status() === 201,
    );

    await bookingPage.startReservation();
    await bookingPage.fillGuestDetails(bookingData);
    await bookingPage.submitReservation();

    const bookingResponse = await bookingResponsePromise;
    const created = (await bookingResponse.json()) as Booking;
    createdBookingId = created.bookingid;
    automationApi.trackBooking(createdBookingId);

    expect(createdBookingId).toBeGreaterThan(0);
    expect(created.roomid).toBe(bookingData.roomid);
    expect(created.firstname).toBe(bookingData.firstname);
    expect(created.lastname).toBe(bookingData.lastname);

    await bookingPage.expectBookingConfirmation(bookingData.bookingdates.checkin, bookingData.bookingdates.checkout);
  });

  await test.step('Verify the persisted booking state via API', async () => {
    const authToken = await automationApi.authenticate();
    const getResponse = await automationApi.getBooking(createdBookingId, authToken);
    expect(getResponse.status()).toBe(200);

    const persisted = (await getResponse.json()) as Booking;
    expect(persisted.bookingid).toBe(createdBookingId);
    expect(persisted.roomid).toBe(bookingData.roomid);
    expect(persisted.firstname).toBe(bookingData.firstname);
    expect(persisted.lastname).toBe(bookingData.lastname);
    expect(persisted.depositpaid).toBe(false);
    expect(persisted.bookingdates.checkin).toBe(bookingData.bookingdates.checkin);
    expect(persisted.bookingdates.checkout).toBe(bookingData.bookingdates.checkout);
  });
});
