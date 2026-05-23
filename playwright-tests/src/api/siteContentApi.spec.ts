import { test, expect } from '../fixtures/automationInTesting.fixture';
import { BrandingResponse, MessagesResponse } from '../models/automationInTesting';
import { expectJsonResponse } from '../utils/apiAssertions';
import { brandingResponseSchema, messagesResponseSchema } from '../schemas/automationInTestingSchemas';
import { expectMatchesSchema } from '../utils/schemaAssertions';

test.describe('Automation in Testing Site Content API', () => {
  test('@api GET /api/branding should return hotel branding', async ({ automationApi }) => {
    const response = await automationApi.getBranding();

    expect(response.status()).toBe(200);
    expectJsonResponse(response);
    const body = (await response.json()) as BrandingResponse;
    expectMatchesSchema(body, brandingResponseSchema);
    expect(body.name).toBeTruthy();
  });

  test('@api GET /api/message should return message summaries', async ({ automationApi }) => {
    const response = await automationApi.getMessages();

    expect(response.status()).toBe(200);
    expectJsonResponse(response);
    const body = (await response.json()) as MessagesResponse;
    expectMatchesSchema(body, messagesResponseSchema);
    expect(body.messages.length).toBeGreaterThan(0);
  });
});
