import { test, expect } from '../fixtures/automationInTesting.fixture';
import { Booking } from '../models/automationInTesting';
import { AutomationInTestingTestData } from '../utils/automationInTestingTestData';

test.describe('API - booking consistency', () => {
  test('@regression @api created booking can be fetched with matching persisted data', async ({ automationApi }) => {
    const bookingData = AutomationInTestingTestData.randomBooking();
    const createResponse = await automationApi.createBooking(bookingData);
    expect(createResponse.status()).toBe(201);

    const created = (await createResponse.json()) as Booking;
    automationApi.trackBooking(created.bookingid);
    expect(created.bookingid).toEqual(expect.any(Number));

    const authToken = await automationApi.authenticate();
    const getResponse = await automationApi.getBooking(created.bookingid, authToken);
    expect(getResponse.status()).toBe(200);

    const fetched = (await getResponse.json()) as Booking;
    expect(fetched.firstname).toBe(bookingData.firstname);
    expect(fetched.lastname).toBe(bookingData.lastname);
    expect(fetched.roomid).toBe(bookingData.roomid);
    expect(fetched.bookingdates.checkin).toBe(bookingData.bookingdates.checkin);
    expect(fetched.bookingdates.checkout).toBe(bookingData.bookingdates.checkout);
  });
});
