import { APIResponse, expect } from '@playwright/test';
import { Booking, BookingUpdateResponse } from '../models/automationInTesting';

const ISO_DATE_PATTERN = /^\d{4}-\d{2}-\d{2}$/;

export function expectJsonResponse(response: APIResponse): void {
  expect(response.headers()['content-type'] ?? '').toMatch(/^application\/(.+\+)?json/);
}

export function expectBookingContract(booking: Booking): void {
  expect(booking.bookingid).toEqual(expect.any(Number));
  expect(booking.bookingid).toBeGreaterThan(0);
  expect(booking.roomid).toEqual(expect.any(Number));
  expect(booking.roomid).toBeGreaterThan(0);
  expect(booking.firstname).toEqual(expect.any(String));
  expect(booking.lastname).toEqual(expect.any(String));
  expect(booking.depositpaid).toEqual(expect.any(Boolean));
  expect(booking.bookingdates).toEqual({
    checkin: expect.stringMatching(ISO_DATE_PATTERN),
    checkout: expect.stringMatching(ISO_DATE_PATTERN),
  });
}

export function expectBookingUpdateResponseContract(response: BookingUpdateResponse): void {
  expect(response.bookingid).toEqual(expect.any(Number));
  expect(response.bookingid).toBeGreaterThan(0);
  expectBookingContract(response.booking);
}
