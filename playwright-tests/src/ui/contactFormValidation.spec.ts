import { test } from '@playwright/test';
import { ContactPage } from '../pages/ContactPage';

test('UI - contact form rejects invalid required-field data @ui', async ({ page }) => {
  const contactPage = new ContactPage(page);
  await contactPage.goto();

  await test.step('Submit invalid contact data', async () => {
    await contactPage.submitMessage({
      name: 'A',
      email: 'not-an-email',
      phone: '123',
      subject: 'No',
      message: 'Too short',
    });
  });

  await test.step('Verify validation feedback', async () => {
    await contactPage.expectValidationFeedback();
  });
});
