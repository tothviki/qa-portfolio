import { test, expect } from '../fixtures/automationInTesting.fixture';
import { AutomationInTestingTestData } from '../utils/automationInTestingTestData';
import { Booking } from '../models/automationInTesting';

test.describe('Automation in Testing Booking API - delete', () => {
  test('@smoke @api DELETE /api/booking/{id} should delete existing booking', async ({ automationApi }) => {
    const createResponse = await automationApi.createBooking(AutomationInTestingTestData.randomBooking());
    expect(createResponse.status()).toBe(201);
    const created = (await createResponse.json()) as Booking;
    automationApi.trackBooking(created.bookingid);
    const authToken = await automationApi.authenticate();

    const deleteResponse = await automationApi.deleteBooking(created.bookingid, authToken);

    expect(deleteResponse.status()).toBe(202);
    automationApi.untrackBooking(created.bookingid);
  });

  test('@regression @api DELETE /api/booking/{id} should fail without auth', async ({ automationApi }) => {
    const createResponse = await automationApi.createBooking(AutomationInTestingTestData.randomBooking());
    expect(createResponse.status()).toBe(201);
    const created = (await createResponse.json()) as Booking;
    automationApi.trackBooking(created.bookingid);

    const response = await automationApi.deleteBooking(created.bookingid, '');

    expect(response.status()).toBe(403);
  });
});
