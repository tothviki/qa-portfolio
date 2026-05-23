import { test, expect } from '../fixtures/automationInTesting.fixture';
import { HealthResponse } from '../models/automationInTesting';
import { expectJsonResponse } from '../utils/apiAssertions';
import { healthResponseSchema } from '../schemas/automationInTestingSchemas';
import { expectMatchesSchema } from '../utils/schemaAssertions';

test.describe('Automation in Testing Health API', () => {
  test('@smoke @api GET /api/booking/actuator/health should return UP status', async ({ automationApi }) => {
    const response = await automationApi.healthCheck();

    expect(response.status()).toBe(200);
    expectJsonResponse(response);
    const body = (await response.json()) as HealthResponse;
    expectMatchesSchema(body, healthResponseSchema);
    expect(body.status).toBe('UP');
  });
});
