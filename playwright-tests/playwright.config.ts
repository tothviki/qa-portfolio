import { defineConfig, devices } from '@playwright/test';
import { AUTOMATION_IN_TESTING_BASE_URL } from './src/config/automationInTestingConfig';

const isCi = !!process.env.CI;

export default defineConfig({
  testDir: './src',

  fullyParallel: true,
  workers: isCi ? 2 : undefined,
  forbidOnly: isCi,

  timeout: 60000,
  expect: { timeout: 10000 },
  retries: isCi ? 2 : 0,

  reporter: [
    ['html', { outputFolder: 'test-reports/html' }],
    ['json', { outputFile: 'test-reports/results.json' }],
    ['list'],
  ],

  use: {
    baseURL: AUTOMATION_IN_TESTING_BASE_URL,
    screenshot: 'only-on-failure',
    video: 'retain-on-failure',
    trace: 'retain-on-failure',
    actionTimeout: 15000,
    navigationTimeout: 30000,
  },

  projects: [
    {
      name: 'api',
      testMatch: /.*\/api\/.*\.spec\.ts/,
      outputDir: 'test-results/api',
    },
    {
      name: 'ui-chrome',
      testMatch: /.*\/ui\/.*\.spec\.ts/,
      outputDir: 'test-results/ui-chrome',
      use: { ...devices['Desktop Chrome'] },
    },
    {
      name: 'ui-firefox',
      testMatch: /.*\/ui\/.*\.spec\.ts/,
      outputDir: 'test-results/ui-firefox',
      use: { ...devices['Desktop Firefox'] },
    },
    {
      name: 'ui-webkit',
      testMatch: /.*\/ui\/.*\.spec\.ts/,
      outputDir: 'test-results/ui-webkit',
      use: { ...devices['Desktop Safari'] },
    },
    {
      name: 'e2e',
      testMatch: /.*\/e2e\/.*\.spec\.ts/,
      outputDir: 'test-results/e2e-chrome',
      use: { ...devices['Desktop Chrome'] },
    },
    {
      name: 'e2e-firefox',
      testMatch: /.*\/e2e\/.*\.spec\.ts/,
      outputDir: 'test-results/e2e-firefox',
      use: { ...devices['Desktop Firefox'] },
    },
    {
      name: 'e2e-webkit',
      testMatch: /.*\/e2e\/.*\.spec\.ts/,
      outputDir: 'test-results/e2e-webkit',
      use: { ...devices['Desktop Safari'] },
    },
  ],
});
