import { test, expect } from '../fixtures/automationInTesting.fixture';
import { AutomationInTestingTestData } from '../utils/automationInTestingTestData';
import { Booking, BookingUpdateResponse, MethodNotAllowedResponse } from '../models/automationInTesting';
import { expectBookingUpdateResponseContract, expectJsonResponse } from '../utils/apiAssertions';
import { bookingUpdateResponseSchema, methodNotAllowedSchema } from '../schemas/automationInTestingSchemas';
import { expectMatchesSchema } from '../utils/schemaAssertions';

test.describe('Automation in Testing Booking API - update', () => {
  test('@smoke @api PUT /api/booking/{id} should fully update booking', async ({ automationApi }) => {
    const initialDates = AutomationInTestingTestData.futureDates(35, 2);
    const updatedDates = AutomationInTestingTestData.futureDates(42, 3);
    const createResponse = await automationApi.createBooking({
      ...AutomationInTestingTestData.namedBooking('TempFirst', 'TempLast'),
      bookingdates: initialDates,
    });
    expect(createResponse.status()).toBe(201);
    const created = (await createResponse.json()) as Booking;
    automationApi.trackBooking(created.bookingid);
    const authToken = await automationApi.authenticate();
    const updatedData = {
      ...AutomationInTestingTestData.updatedBooking(created.roomid),
      bookingdates: updatedDates,
    };

    const updateResponse = await automationApi.updateBooking(created.bookingid, updatedData, authToken);

    expect(updateResponse.status()).toBe(200);
    expectJsonResponse(updateResponse);
    const updated = (await updateResponse.json()) as BookingUpdateResponse;
    expectMatchesSchema(updated, bookingUpdateResponseSchema);
    expectBookingUpdateResponseContract(updated);
    expect(updated.bookingid).toBe(created.bookingid);
    expect(updated.booking.firstname).toBe(updatedData.firstname);
    expect(updated.booking.lastname).toBe(updatedData.lastname);
    expect(updated.booking.depositpaid).toBe(updatedData.depositpaid);
    expect(updated.booking.bookingdates.checkin).toBe(updatedData.bookingdates.checkin);
    expect(updated.booking.bookingdates.checkout).toBe(updatedData.bookingdates.checkout);
  });

  test('@api PATCH /api/booking/{id} should document unsupported method', async ({ automationApi }) => {
    const createResponse = await automationApi.createBooking(AutomationInTestingTestData.randomBooking());
    expect(createResponse.status()).toBe(201);
    const created = (await createResponse.json()) as Booking;
    automationApi.trackBooking(created.bookingid);
    const authToken = await automationApi.authenticate();

    const response = await automationApi.patchBooking(created.bookingid, { firstname: 'PatchedFirst' }, authToken);

    expect(response.status()).toBe(405);
    expectJsonResponse(response);
    const body = (await response.json()) as MethodNotAllowedResponse;
    expectMatchesSchema(body, methodNotAllowedSchema);
    expect(body.error).toBe('Method Not Allowed');
  });
});
