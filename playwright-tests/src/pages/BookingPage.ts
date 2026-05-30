import { expect, Locator, Page } from '@playwright/test';
import { futureDate } from '../ui/uiHelpers';

export class BookingPage {
  readonly availabilityHeading: Locator;
  readonly roomListHeading: Locator;
  readonly checkAvailabilityButton: Locator;
  readonly checkinInput: Locator;
  readonly checkoutInput: Locator;

  constructor(private readonly page: Page) {
    const bookingSection = page.locator('#booking');

    this.availabilityHeading = page.getByRole('heading', { name: 'Check Availability & Book Your Stay' });
    this.roomListHeading = page.getByRole('heading', { name: 'Our Rooms' });
    this.checkAvailabilityButton = page.getByRole('button', { name: 'Check Availability' });
    this.checkinInput = bookingSection
      .locator(
        'input#checkin, input[name="checkin"], input[placeholder*="Check In" i], input[aria-label*="check in" i]',
      )
      .or(bookingSection.getByRole('textbox').nth(0))
      .first();
    this.checkoutInput = bookingSection
      .locator(
        'input#checkout, input[name="checkout"], input[placeholder*="Check Out" i], input[aria-label*="check out" i]',
      )
      .or(bookingSection.getByRole('textbox').nth(1))
      .first();
  }

  async goto(): Promise<void> {
    await this.page.goto('/#booking');
    await expect(this.availabilityHeading, 'Booking availability heading should be visible').toBeVisible();
  }

  async searchAvailability(checkin: string, checkout: string): Promise<void> {
    await expect(this.checkinInput, 'Check-in date input should be visible').toBeVisible();
    await expect(this.checkoutInput, 'Check-out date input should be visible').toBeVisible();
    await this.checkinInput.fill(checkin);
    await this.checkoutInput.fill(checkout);
    await expect(this.checkAvailabilityButton, 'Check Availability button should be enabled').toBeEnabled();
    await this.checkAvailabilityButton.click();
  }

  async expectBookNowLinks(count?: number): Promise<void> {
    const links = this.bookNowLinks;
    await expect(links.first(), 'At least one room should expose a Book now link').toBeVisible();
    if (count !== undefined) {
      await expect(links, `Expected ${count} Book now links`).toHaveCount(count);
    }
  }

  async expectRoomSummaries(): Promise<void> {
    await expect(this.roomListHeading).toBeVisible();
    await expect(this.page.getByRole('heading', { name: 'Single' })).toBeVisible();
    await expect(this.page.getByRole('heading', { name: 'Double' })).toBeVisible();
    await expect(this.page.getByRole('heading', { name: 'Suite' })).toBeVisible();
  }

  async expectPriceSummaries(): Promise<void> {
    const roomCards = this.page.locator('#rooms');
    await expect(roomCards.getByText(/per night/i), 'Each room card should show a nightly price').toHaveCount(3);
  }

  async expectFirstBookingLinkDates(checkin: string, checkout: string): Promise<void> {
    await expect(this.bookNowLinks.first()).toHaveAttribute(
      'href',
      new RegExp(`checkin=${checkin}.*checkout=${checkout}`),
    );
  }

  async openFirstRoomReservation(): Promise<void> {
    const firstRoomLink = this.bookNowLinks.first();
    await expect(firstRoomLink, 'First room should have a Book now link').toBeVisible();
    const reservationUrl = await firstRoomLink.getAttribute('href');
    expect(reservationUrl, 'First room reservation link should have an href').toBeTruthy();
    await this.page.goto(reservationUrl!);
    await this.expectReservationPage();
  }

  async expectReservationPage(): Promise<void> {
    await expect(this.page.getByRole('heading', { name: /Single|Double|Suite/ }).first()).toBeVisible();
    await expect(this.page.getByRole('heading', { name: 'Book This Room' })).toBeVisible();
    await expect(this.page.getByText('Price Summary')).toBeVisible();
    await expect(this.reserveNowButton).toBeEnabled();
  }

  async startReservation(): Promise<void> {
    await expect(this.reserveNowButton, 'Reserve Now button should be visible').toBeVisible();
    await this.reserveNowButton.click();
  }

  async cancelStartedReservation(): Promise<void> {
    await expect(this.cancelButton, 'Cancel button should be visible after reservation starts').toBeVisible();
    await this.cancelButton.click();
    await expect(this.reserveNowButton).toBeVisible();
    await expect(this.cancelButton).toHaveCount(0);
  }

  async expectBookedRoomUnavailable(roomId: number): Promise<void> {
    const bookedRoomLink = this.roomReservationLink(roomId);
    await expect(bookedRoomLink, 'Booked room should not be shown as reservable').toHaveCount(0);
  }

  async expectBookedRoomAvailable(roomId: number): Promise<void> {
    const roomLink = this.roomReservationLink(roomId);
    await expect(roomLink, 'Cancelled room should be shown as reservable again').toHaveCount(1);
    await expect(roomLink.first()).toBeVisible();
  }

  async expectInvalidDateFeedback(): Promise<void> {
    const invalidDateFeedback = this.page.locator('[role="alert"], .alert-danger, .invalid-feedback').filter({
      hasText: /check.?out|date|after|before|valid/i,
    });
    await expect(invalidDateFeedback.first(), 'Invalid date range should show validation feedback').toBeVisible();
  }

  async expectRoomSearchResults(): Promise<void> {
    await this.expectBookNowLinks();
    await this.expectRoomSummaries();
  }

  nextAvailableDates(startOffset = 1, stayLength = 1): { checkin: string; checkout: string } {
    return {
      checkin: futureDate(startOffset),
      checkout: futureDate(startOffset + stayLength),
    };
  }

  get bookNowLinks(): Locator {
    // Room reservation links are the only "Book now" links with reservation URLs; the hero CTA also uses similar text.
    return this.page.locator('a[href^="/reservation/"]').filter({ hasText: /^Book now$/i });
  }

  private roomReservationLink(roomId: number): Locator {
    return this.page.locator(`a[href*="/reservation/${roomId}"]`).filter({ hasText: /^Book now$/i });
  }

  get reserveNowButton(): Locator {
    return this.page.getByRole('button', { name: 'Reserve Now' });
  }

  get cancelButton(): Locator {
    return this.page.getByRole('button', { name: 'Cancel' });
  }
}
