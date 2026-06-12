import { test, expect } from '../fixtures/automationInTesting.fixture';
import { Message, MessagesResponse } from '../models/automationInTesting';
import { ContactPage } from '../pages/ContactPage';
import { AutomationInTestingTestData } from '../utils/automationInTestingTestData';

test('E2E - contact form submission appears in message API results @e2e @regression', async ({ page, automationApi }) => {
  const contactPage = new ContactPage(page);
  const uniqueId = AutomationInTestingTestData.nextId('contact-e2e');
  const messageData = {
    name: `Contact ${uniqueId}`,
    email: `${uniqueId}@example.com`,
    phone: '07123456789',
    subject: `Subject ${uniqueId}`,
    message: `Automated contact message ${uniqueId} for API propagation verification.`,
  };

  await test.step('Submit a contact message in the public UI', async () => {
    await contactPage.goto();
    await contactPage.submitMessage(messageData);
    await contactPage.expectSuccessMessage();
  });

  await test.step('Verify the submitted message appears in the message API', async () => {
    await expect
      .poll(
        async () => {
          const response = await automationApi.getMessages();
          expect(response.status()).toBe(200);

          const body = (await response.json()) as MessagesResponse;
          return body.messages.find(
            (message: Message) => message.name === messageData.name && message.subject === messageData.subject,
          );
        },
        {
          message: `Expected contact message ${messageData.subject} to appear in GET /api/message results`,
          intervals: [1_000, 2_000, 3_000],
          timeout: 15_000,
        },
      )
      .toMatchObject({
        name: messageData.name,
        subject: messageData.subject,
        read: false,
      });
  });
});
