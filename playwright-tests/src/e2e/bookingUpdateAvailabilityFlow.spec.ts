import { test, expect } from '../fixtures/automationInTesting.fixture';
import { Booking, UpdateBookingPayload } from '../models/automationInTesting';
import { BookingPage } from '../pages/BookingPage';
import { AutomationInTestingTestData } from '../utils/automationInTestingTestData';

test('E2E - API booking update changes public room availability for old and new dates @e2e @regression', async ({
  page,
  browserName,
  automationApi,
}) => {
  const bookingPage = new BookingPage(page);
  const dateOffsetByBrowser = {
    chromium: 7,
    firefox: 37,
    webkit: 67,
  } as const;
  const browserStartOffset = dateOffsetByBrowser[browserName];
  const createdBookingData = AutomationInTestingTestData.randomBooking(1);
  let updatedBookingData: UpdateBookingPayload | undefined;
  let bookingId = 0;

  async function expectRoomAvailability(checkin: string, checkout: string, roomId: number, available: boolean): Promise<void> {
    await expect
      .poll(
        async () => {
          await bookingPage.goto();
          await bookingPage.searchAvailability(checkin, checkout);
          return bookingPage.hasBookableRoom(roomId);
        },
        {
          message: `Expected room ${roomId} availability for ${checkin} to ${checkout} to become ${available}`,
          intervals: [500, 1000, 2000],
          timeout: 10000,
        },
      )
      .toBe(available);
  }

  await test.step('Find an initial future date range that is available in the UI and can be booked through the API', async () => {
    await bookingPage.goto();

    for (let startOffset = browserStartOffset; startOffset <= browserStartOffset + 15; startOffset += 3) {
      const candidateDates = bookingPage.nextAvailableDates(startOffset, 2);
      await bookingPage.searchAvailability(candidateDates.checkin, candidateDates.checkout);

      if (!(await bookingPage.hasBookableRoom(createdBookingData.roomid))) {
        await bookingPage.goto();
        continue;
      }

      createdBookingData.bookingdates = candidateDates;
      const createResponse = await automationApi.createBooking(createdBookingData);

      if (createResponse.status() === 201) {
        const created = (await createResponse.json()) as Booking;
        bookingId = created.bookingid;
        automationApi.trackBooking(bookingId);

        expect(created.roomid).toBe(createdBookingData.roomid);
        expect(created.bookingdates.checkin).toBe(createdBookingData.bookingdates.checkin);
        expect(created.bookingdates.checkout).toBe(createdBookingData.bookingdates.checkout);
        break;
      }

      expect(
        createResponse.status(),
        `Expected a successful booking or a conflict while probing ${candidateDates.checkin} to ${candidateDates.checkout}`,
      ).toBe(409);
      await bookingPage.goto();
    }

    expect(
      createdBookingData.bookingdates,
      'Expected to find an initial future date range that is available before create',
    ).toBeDefined();
    expect(bookingId).toBeGreaterThan(0);
  });

  await test.step('Verify the original dates hide the room in the public UI', async () => {
    await expectRoomAvailability(
      createdBookingData.bookingdates.checkin,
      createdBookingData.bookingdates.checkout,
      createdBookingData.roomid,
      false,
    );
  });

  await test.step('Find a future date range where the room is publicly available before updating', async () => {
    await bookingPage.goto();

    for (let startOffset = browserStartOffset + 20; startOffset <= browserStartOffset + 35; startOffset += 3) {
      const candidateDates = bookingPage.nextAvailableDates(startOffset, 2);
      await bookingPage.searchAvailability(candidateDates.checkin, candidateDates.checkout);

      if (await bookingPage.hasBookableRoom(createdBookingData.roomid)) {
        updatedBookingData = {
          ...AutomationInTestingTestData.updatedBooking(createdBookingData.roomid),
          roomid: createdBookingData.roomid,
          bookingdates: candidateDates,
        };
        break;
      }

      await bookingPage.goto();
    }

    expect(updatedBookingData, 'Expected to find a future date range where the room is available before update').toBeDefined();
  });

  await test.step('Update the booking dates through the API', async () => {
    const authToken = await automationApi.authenticate();
    const updateResponse = await automationApi.updateBooking(bookingId, updatedBookingData!, authToken);
    expect(updateResponse.status()).toBe(200);

    const updated = (await updateResponse.json()) as { bookingid: number; booking: Booking };
    expect(updated.bookingid).toBe(bookingId);
    expect(updated.booking.bookingdates.checkin).toBe(updatedBookingData!.bookingdates.checkin);
    expect(updated.booking.bookingdates.checkout).toBe(updatedBookingData!.bookingdates.checkout);
  });

  await test.step('Verify the original dates become available again', async () => {
    await expectRoomAvailability(
      createdBookingData.bookingdates.checkin,
      createdBookingData.bookingdates.checkout,
      createdBookingData.roomid,
      true,
    );
  });

  await test.step('Verify the updated dates now hide the room in the public UI', async () => {
    await expectRoomAvailability(
      updatedBookingData!.bookingdates.checkin,
      updatedBookingData!.bookingdates.checkout,
      updatedBookingData!.roomid,
      false,
    );
  });
});
