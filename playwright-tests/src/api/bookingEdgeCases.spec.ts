import { test, expect } from '../fixtures/automationInTesting.fixture';
import { Booking, ErrorResponse, InvalidBookingPayload, ValidationErrorsResponse } from '../models/automationInTesting';
import { bookingSchema, errorResponseSchema, validationErrorsSchema } from '../schemas/automationInTestingSchemas';
import { expectJsonResponse } from '../utils/apiAssertions';
import { AutomationInTestingTestData } from '../utils/automationInTestingTestData';
import { expectMatchesSchema } from '../utils/schemaAssertions';

test.describe('Automation in Testing Booking API - edge cases', () => {
  test('@regression @api POST /api/booking should reject checkout before checkin', async ({ automationApi }) => {
    const response = await automationApi.createBooking({
      ...AutomationInTestingTestData.randomBooking(),
      bookingdates: {
        checkin: '2028-01-12',
        checkout: '2028-01-10',
      },
    });

    expect(response.status()).toBe(409);
    expectJsonResponse(response);
    const body = (await response.json()) as ErrorResponse;
    expectMatchesSchema(body, errorResponseSchema);
    expect(body.error).toBe('Failed to create booking');
  });

  test('@regression @api POST /api/booking should reject each missing required guest name field', async ({
    automationApi,
  }) => {
    const basePayload = AutomationInTestingTestData.randomBooking();
    const missingFirstName: InvalidBookingPayload = { ...basePayload };
    delete missingFirstName.firstname;
    const missingLastName: InvalidBookingPayload = { ...basePayload };
    delete missingLastName.lastname;

    for (const payload of [missingFirstName, missingLastName]) {
      const response = await automationApi.postBookingPayload(payload);

      expect(response.status()).toBe(400);
      expectJsonResponse(response);
      const body = (await response.json()) as ValidationErrorsResponse;
      expectMatchesSchema(body, validationErrorsSchema);
      expect(body.errors.length).toBeGreaterThan(0);
    }
  });

  test('@regression @api POST /api/booking currently accepts unknown room IDs - documented product behavior', async ({
    automationApi,
  }) => {
    const response = await automationApi.createBooking(AutomationInTestingTestData.randomBooking(9999));

    expect(response.status()).toBe(201);
    expectJsonResponse(response);
    const created = (await response.json()) as Booking;
    automationApi.trackBooking(created.bookingid);
    expectMatchesSchema(created, bookingSchema);
    expect(created.roomid).toBe(9999);
  });

  test('@regression @api POST /api/booking should reject malformed JSON', async ({ automationApi }) => {
    const response = await automationApi.postRawBookingPayload('{roomid:');

    expect(response.status()).toBe(400);
  });

  test('@regression @api DELETE /api/booking/{id} should reject invalid auth token', async ({ automationApi }) => {
    const createResponse = await automationApi.createBooking(AutomationInTestingTestData.randomBooking());
    expect(createResponse.status()).toBe(201);
    const created = (await createResponse.json()) as Booking;
    automationApi.trackBooking(created.bookingid);

    const response = await automationApi.deleteBooking(created.bookingid, 'not-a-real-token');

    expect(response.status()).toBe(403);
  });
});
