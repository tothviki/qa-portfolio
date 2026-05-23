import { test, expect } from '../fixtures/automationInTesting.fixture';
import { AutomationInTestingTestData } from '../utils/automationInTestingTestData';
import { Booking, BookingListResponse, ErrorResponse } from '../models/automationInTesting';
import { expectBookingContract, expectJsonResponse } from '../utils/apiAssertions';
import { bookingListResponseSchema, bookingSchema, errorResponseSchema } from '../schemas/automationInTestingSchemas';
import { expectMatchesSchema } from '../utils/schemaAssertions';

test.describe('Automation in Testing Booking API - read', () => {
  test('@smoke @api GET /api/booking/{id} should get booking by valid ID', async ({ automationApi }) => {
    const bookingData = AutomationInTestingTestData.randomBooking();
    const createResponse = await automationApi.createBooking(bookingData);
    expect(createResponse.status()).toBe(201);
    const created = (await createResponse.json()) as Booking;
    automationApi.trackBooking(created.bookingid);
    const authToken = await automationApi.authenticate();

    const getResponse = await automationApi.getBooking(created.bookingid, authToken);

    expect(getResponse.status()).toBe(200);
    expectJsonResponse(getResponse);
    const booking = (await getResponse.json()) as Booking;
    expectMatchesSchema(booking, bookingSchema);
    expectBookingContract(booking);
    expect(booking.firstname).toBe(bookingData.firstname);
    expect(booking.lastname).toBe(bookingData.lastname);
    expect(booking.roomid).toBe(bookingData.roomid);
  });

  test('@api GET /api/booking should list bookings by room id', async ({ automationApi }) => {
    const bookingData = AutomationInTestingTestData.randomBooking();
    const createResponse = await automationApi.createBooking(bookingData);
    expect(createResponse.status()).toBe(201);
    const created = (await createResponse.json()) as Booking;
    automationApi.trackBooking(created.bookingid);
    const authToken = await automationApi.authenticate();

    const response = await automationApi.getBookingsByRoom(bookingData.roomid, authToken);

    expect(response.status()).toBe(200);
    expectJsonResponse(response);
    const body = (await response.json()) as BookingListResponse;
    expectMatchesSchema(body, bookingListResponseSchema);
    expect(body.bookings.some(({ bookingid }) => bookingid === created.bookingid)).toBe(true);
  });

  test('@regression @api GET /api/booking should require authentication', async ({ automationApi }) => {
    const response = await automationApi.getBookingsWithoutAuth(1);

    expect(response.status()).toBe(401);
    expectJsonResponse(response);
    const body = (await response.json()) as ErrorResponse;
    expectMatchesSchema(body, errorResponseSchema);
    expect(body.error).toBe('Authentication required');
  });

  test('@regression @api GET /api/booking should require room id filter', async ({ automationApi }) => {
    const authToken = await automationApi.authenticate();
    const response = await automationApi.getBookingsWithoutRoom(authToken);

    expect(response.status()).toBe(400);
    expectJsonResponse(response);
    const body = (await response.json()) as ErrorResponse;
    expectMatchesSchema(body, errorResponseSchema);
    expect(body.error).toBe('Room ID is required');
  });
});
