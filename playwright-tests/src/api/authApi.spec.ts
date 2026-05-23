import { test, expect } from '../fixtures/automationInTesting.fixture';
import { AuthTokenResponse, ErrorResponse } from '../models/automationInTesting';
import { expectJsonResponse } from '../utils/apiAssertions';
import { authTokenSchema, errorResponseSchema } from '../schemas/automationInTestingSchemas';
import { expectMatchesSchema } from '../utils/schemaAssertions';

test.describe('Automation in Testing Auth API', () => {
  test('@smoke @api POST /api/auth/login should create token with valid credentials', async ({ automationApi }) => {
    const response = await automationApi.createToken();

    expect(response.status()).toBe(200);
    expectJsonResponse(response);
    const body = (await response.json()) as AuthTokenResponse;
    expectMatchesSchema(body, authTokenSchema);
    expect(body.token).toEqual(expect.any(String));
    expect(body.token.length).toBeGreaterThan(0);
  });

  test('@regression @api POST /api/auth/login should reject invalid credentials', async ({ automationApi }) => {
    const response = await automationApi.createToken({
      username: 'admin',
      password: 'wrong-password',
    });

    expect(response.status()).toBe(401);
    expectJsonResponse(response);
    const body = (await response.json()) as ErrorResponse;
    expectMatchesSchema(body, errorResponseSchema);
    expect(body.error).toBe('Invalid credentials');
  });
});
