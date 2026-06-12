import { test, expect } from '../fixtures/automationInTesting.fixture';
import { Booking, CreateBookingPayload, ValidationErrorsResponse } from '../models/automationInTesting';
import { BookingPage } from '../pages/BookingPage';
import { AutomationInTestingTestData } from '../utils/automationInTestingTestData';

test('E2E - invalid public reservation is rejected and does not persist a booking @e2e @regression', async ({
  page,
  automationApi,
}) => {
  const bookingPage = new BookingPage(page);
  const validBookingData = AutomationInTestingTestData.randomBooking(1);
  const invalidBookingData: CreateBookingPayload = {
    ...validBookingData,
    firstname: 'A',
    lastname: '',
    email: 'bad',
    phone: '123',
  };

  await test.step('Open the public reservation journey for the selected dates', async () => {
    await bookingPage.goto();
    await bookingPage.searchAvailability(validBookingData.bookingdates.checkin, validBookingData.bookingdates.checkout);
    await bookingPage.expectBookNowLinks();
    await bookingPage.openFirstRoomReservation();
  });

  await test.step('Submit invalid guest data and verify validation feedback', async () => {
    const failedBookingResponsePromise = page.waitForResponse(
      (response) =>
        response.url().includes('/api/booking') &&
        response.request().method() === 'POST' &&
        response.status() === 400,
    );

    await bookingPage.startReservation();
    await bookingPage.fillGuestDetails(invalidBookingData);
    await bookingPage.submitReservation();

    const failedBookingResponse = await failedBookingResponsePromise;
    const body = (await failedBookingResponse.json()) as ValidationErrorsResponse;

    expect(body.errors.length).toBeGreaterThan(0);
    await bookingPage.expectReservationValidationErrors([
      'Lastname should not be blank',
      'must be a well-formed email address',
    ]);
  });

  await test.step('Verify no invalid booking was persisted in the API', async () => {
    const authToken = await automationApi.authenticate();
    const listResponse = await automationApi.getBookingsByRoom(validBookingData.roomid, authToken);
    expect(listResponse.status()).toBe(200);

    const body = (await listResponse.json()) as { bookings: Booking[] };
    const persistedInvalidBooking = body.bookings.find(
      (booking) =>
        booking.firstname === invalidBookingData.firstname &&
        booking.lastname === invalidBookingData.lastname &&
        booking.bookingdates.checkin === invalidBookingData.bookingdates.checkin &&
        booking.bookingdates.checkout === invalidBookingData.bookingdates.checkout,
    );

    expect(persistedInvalidBooking).toBeUndefined();
  });
});
