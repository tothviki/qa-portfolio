import { expect, Locator, Page } from '@playwright/test';

export class ContactPage {
  readonly heading: Locator;

  constructor(private readonly page: Page) {
    this.heading = page.getByRole('heading', { name: 'Send Us a Message' });
  }

  async goto(): Promise<void> {
    await this.page.goto('/#contact');
    await expect(this.heading, 'Contact form heading should be visible').toBeVisible();
  }

  async submitMessage(data: ContactMessage): Promise<void> {
    await this.fillMessage(data);
    await this.submit();
  }

  async fillMessage(data: ContactMessage): Promise<void> {
    await this.page.getByRole('textbox', { name: 'Name' }).fill(data.name);
    await this.page.getByRole('textbox', { name: 'Email' }).fill(data.email);
    await this.page.getByRole('textbox', { name: 'Phone' }).fill(data.phone);
    await this.page.getByRole('textbox', { name: 'Subject' }).fill(data.subject);

    // The message textarea has no accessible name in the current public UI.
    await expect(this.messageInput, 'Message textarea should be visible').toBeVisible();
    await this.messageInput.fill(data.message);
  }

  async submit(): Promise<void> {
    await expect(this.submitButton, 'Contact Submit button should be enabled').toBeEnabled();
    await this.submitButton.click();
  }

  async expectSuccessMessage(): Promise<void> {
    await expect(
      this.page.getByText(/thanks|thank you|message sent/i).first(),
      'Contact form should show a success acknowledgement',
    ).toBeVisible();
  }

  async expectValidationFeedback(): Promise<void> {
    const validationErrors = this.page.locator('.alert-danger, .invalid-feedback, [role="alert"]').filter({
      hasText: /must|valid|error|required|between|phone|email/i,
    });
    await expect(validationErrors.first(), 'Invalid contact submission should show validation feedback').toBeVisible();
  }

  private get submitButton(): Locator {
    return this.page.getByRole('button', { name: 'Submit' });
  }

  private get messageInput(): Locator {
    return this.page.locator('textarea').first();
  }
}

export type ContactMessage = {
  name: string;
  email: string;
  phone: string;
  subject: string;
  message: string;
};
