import { test as base } from '@playwright/test';
import { AutomationInTestingClient } from '../utils/automationInTestingClient';

type AutomationInTestingFixtures = {
  automationApi: AutomationInTestingClient;
};

export const test = base.extend<AutomationInTestingFixtures>({
  automationApi: async ({ request }, use, testInfo) => {
    const client = new AutomationInTestingClient(request);

    await use(client);

    const cleanup = await client.deleteTrackedBookings();
    if (cleanup.errors.length > 0) {
      const cleanupReport = JSON.stringify(cleanup, null, 2);
      console.warn(`Booking cleanup completed with errors for ${testInfo.title}:\n${cleanupReport}`);
      await testInfo.attach('booking-cleanup-errors.json', {
        body: cleanupReport,
        contentType: 'application/json',
      });
    }
  },
});

export { expect } from '@playwright/test';
