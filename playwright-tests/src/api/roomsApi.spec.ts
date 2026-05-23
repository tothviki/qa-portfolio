import { test, expect } from '../fixtures/automationInTesting.fixture';
import { RoomsResponse } from '../models/automationInTesting';
import { expectJsonResponse } from '../utils/apiAssertions';
import { roomsResponseSchema } from '../schemas/automationInTestingSchemas';
import { expectMatchesSchema } from '../utils/schemaAssertions';

test.describe('Automation in Testing Rooms API', () => {
  test('@smoke @api GET /api/room should return bookable rooms', async ({ automationApi }) => {
    const response = await automationApi.getRooms();

    expect(response.status()).toBe(200);
    expectJsonResponse(response);
    const body = (await response.json()) as RoomsResponse;
    expectMatchesSchema(body, roomsResponseSchema);
    expect(body.rooms.length).toBeGreaterThan(0);
    expect(body.rooms.some(({ accessible }) => accessible)).toBe(true);
  });
});
