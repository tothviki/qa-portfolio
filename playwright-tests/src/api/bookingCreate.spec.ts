import { test, expect } from '../fixtures/automationInTesting.fixture';
import { AutomationInTestingTestData } from '../utils/automationInTestingTestData';
import { Booking, ValidationErrorsResponse } from '../models/automationInTesting';
import { expectBookingContract, expectJsonResponse } from '../utils/apiAssertions';
import { bookingSchema, validationErrorsSchema } from '../schemas/automationInTestingSchemas';
import { expectMatchesSchema } from '../utils/schemaAssertions';

test.describe('Automation in Testing Booking API - create', () => {
  test('@smoke @api POST /api/booking should create booking with valid data', async ({ automationApi }) => {
    const bookingData = AutomationInTestingTestData.randomBooking();
    const response = await automationApi.createBooking(bookingData);

    expect(response.status()).toBe(201);
    expectJsonResponse(response);
    const created = (await response.json()) as Booking;
    automationApi.trackBooking(created.bookingid);
    expectMatchesSchema(created, bookingSchema);
    expectBookingContract(created);
    expect(created.firstname).toBe(bookingData.firstname);
    expect(created.lastname).toBe(bookingData.lastname);
    expect(created.roomid).toBe(bookingData.roomid);
  });

  test('@api POST /api/booking should accept supported punctuation in names', async ({ automationApi }) => {
    const bookingData = AutomationInTestingTestData.bookingWithSupportedCharacters();
    const response = await automationApi.createBooking(bookingData);

    expect(response.status()).toBe(201);
    expectJsonResponse(response);
    const created = (await response.json()) as Booking;
    automationApi.trackBooking(created.bookingid);
    expectMatchesSchema(created, bookingSchema);
    expect(created.firstname).toBe(bookingData.firstname);
    expect(created.lastname).toBe(bookingData.lastname);
  });

  test('@regression @api POST /api/booking should return validation errors for invalid payload', async ({
    automationApi,
  }) => {
    const response = await automationApi.postBookingPayload(AutomationInTestingTestData.invalidBookingPayload());

    expect(response.status()).toBe(400);
    expectJsonResponse(response);
    const body = (await response.json()) as ValidationErrorsResponse;
    expectMatchesSchema(body, validationErrorsSchema);
    expect(body.errors.length).toBeGreaterThan(0);
  });
});
