import { test } from '@playwright/test';
import { ContactPage } from '../pages/ContactPage';
import { randomEmail, randomName } from './uiHelpers';

test('UI - send a contact message @ui', async ({ page }) => {
  const contactPage = new ContactPage(page);
  const name = randomName();
  const email = randomEmail();
  const subject = 'Playwright portfolio message';
  const message = 'Hello from an automated portfolio test. Please ignore.';

  await contactPage.goto();

  await test.step('Fill required contact fields', async () => {
    await contactPage.fillMessage({
      name,
      email,
      phone: '07123456789',
      subject,
      message,
    });
  });

  await test.step('Submit message and verify acknowledgement', async () => {
    await contactPage.submit();
    await contactPage.expectSuccessMessage();
  });
});
