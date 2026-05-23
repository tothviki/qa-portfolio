import { APIRequestContext, APIResponse } from '@playwright/test';
import {
  AuthCredentials,
  AuthTokenResponse,
  CreateBookingPayload,
  InvalidBookingPayload,
  UpdateBookingPayload,
} from '../models/automationInTesting';
import { AUTOMATION_IN_TESTING_BASE_URL } from '../config/automationInTestingConfig';

export type BookingCleanupError = {
  bookingId?: number;
  message: string;
};

export type BookingCleanupResult = {
  deletedBookingIds: number[];
  errors: BookingCleanupError[];
};

const DEFAULT_AUTH_CREDENTIALS: AuthCredentials = { username: 'admin', password: 'password' };

export class AutomationInTestingClient {
  private readonly trackedBookingIds = new Set<number>();
  private authToken?: string;

  constructor(
    private readonly request: APIRequestContext,
    private readonly baseUrl = AUTOMATION_IN_TESTING_BASE_URL,
  ) {}

  createToken(credentials: AuthCredentials = DEFAULT_AUTH_CREDENTIALS): Promise<APIResponse> {
    return this.request.post(`${this.baseUrl}/api/auth/login`, {
      data: credentials,
    });
  }

  async authenticate(): Promise<string> {
    if (this.authToken) {
      return this.authToken;
    }
    const response = await this.createToken();
    if (response.status() !== 200) {
      throw new Error(`Authentication failed with status ${response.status()}`);
    }

    const data = (await response.json()) as AuthTokenResponse;
    this.authToken = data.token;
    return this.authToken;
  }

  createBooking(bookingData: CreateBookingPayload): Promise<APIResponse> {
    return this.postBookingPayload(bookingData);
  }

  postBookingPayload(payload: CreateBookingPayload | InvalidBookingPayload): Promise<APIResponse> {
    return this.request.post(`${this.baseUrl}/api/booking`, {
      data: payload,
      headers: {
        'Content-Type': 'application/json',
      },
    });
  }

  postRawBookingPayload(body: string, contentType = 'application/json'): Promise<APIResponse> {
    return this.request.post(`${this.baseUrl}/api/booking`, {
      data: body,
      headers: {
        'Content-Type': contentType,
      },
    });
  }

  trackBooking(bookingId: number): void {
    this.trackedBookingIds.add(bookingId);
  }

  untrackBooking(bookingId: number): void {
    this.trackedBookingIds.delete(bookingId);
  }

  async deleteTrackedBookings(): Promise<BookingCleanupResult> {
    const result: BookingCleanupResult = {
      deletedBookingIds: [],
      errors: [],
    };

    if (this.trackedBookingIds.size === 0) {
      return result;
    }

    let authToken: string;
    try {
      authToken = await this.authenticate();
    } catch (error) {
      result.errors.push({
        message: `Could not authenticate for booking cleanup: ${error instanceof Error ? error.message : String(error)}`,
      });
      return result;
    }

    for (const bookingId of [...this.trackedBookingIds].reverse()) {
      try {
        const response = await this.deleteBooking(bookingId, authToken);
        if (response.ok()) {
          result.deletedBookingIds.push(bookingId);
          this.untrackBooking(bookingId);
          continue;
        }

        result.errors.push({
          bookingId,
          message: `Delete returned HTTP ${response.status()}: ${await response.text()}`,
        });
      } catch (error) {
        result.errors.push({
          bookingId,
          message: error instanceof Error ? error.message : String(error),
        });
      }
    }

    return result;
  }

  getRooms(): Promise<APIResponse> {
    return this.request.get(`${this.baseUrl}/api/room`);
  }

  getBooking(id: number, authToken: string): Promise<APIResponse> {
    return this.request.get(`${this.baseUrl}/api/booking/${id}`, {
      headers: {
        Cookie: `token=${authToken}`,
      },
    });
  }

  getBookingsByRoom(roomId: number, authToken: string): Promise<APIResponse> {
    return this.request.get(`${this.baseUrl}/api/booking?roomid=${roomId}`, {
      headers: {
        Cookie: `token=${authToken}`,
      },
    });
  }

  getBookingsWithoutRoom(authToken: string): Promise<APIResponse> {
    return this.request.get(`${this.baseUrl}/api/booking`, {
      headers: {
        Cookie: `token=${authToken}`,
      },
    });
  }

  getBookingsWithoutAuth(roomId: number): Promise<APIResponse> {
    return this.request.get(`${this.baseUrl}/api/booking?roomid=${roomId}`);
  }

  updateBooking(id: number, bookingData: UpdateBookingPayload, authToken: string): Promise<APIResponse> {
    return this.request.put(`${this.baseUrl}/api/booking/${id}`, {
      headers: {
        Cookie: `token=${authToken}`,
        'Content-Type': 'application/json',
      },
      data: bookingData,
    });
  }

  patchBooking(id: number, partialData: Partial<UpdateBookingPayload>, authToken: string): Promise<APIResponse> {
    return this.request.patch(`${this.baseUrl}/api/booking/${id}`, {
      headers: {
        Cookie: `token=${authToken}`,
        'Content-Type': 'application/json',
      },
      data: partialData,
    });
  }

  deleteBooking(id: number, authToken: string): Promise<APIResponse> {
    return this.request.delete(`${this.baseUrl}/api/booking/${id}`, {
      headers: {
        Cookie: `token=${authToken}`,
      },
    });
  }

  healthCheck(): Promise<APIResponse> {
    return this.request.get(`${this.baseUrl}/api/booking/actuator/health`);
  }

  getBranding(): Promise<APIResponse> {
    return this.request.get(`${this.baseUrl}/api/branding`);
  }

  getMessages(): Promise<APIResponse> {
    return this.request.get(`${this.baseUrl}/api/message`);
  }
}
