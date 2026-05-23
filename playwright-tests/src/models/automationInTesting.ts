export interface AuthCredentials {
  username: string;
  password: string;
}

export interface AuthTokenResponse {
  token: string;
}

export interface ErrorResponse {
  error: string;
}

export interface ValidationErrorsResponse {
  errors: string[];
}

export interface BookingDates {
  checkin: string;
  checkout: string;
}

export interface Room {
  roomid: number;
  roomName: string;
  type: string;
  accessible: boolean;
  description: string;
  features: string[];
  image: string;
  roomPrice: number;
}

export interface RoomsResponse {
  rooms: Room[];
}

export interface CreateBookingPayload {
  roomid: number;
  firstname: string;
  lastname: string;
  depositpaid: boolean;
  email: string;
  phone: string;
  bookingdates: BookingDates;
  // Added optional totalprice to satisfy TypeScript checks in e2e tests
  totalprice?: number;
}

export interface UpdateBookingPayload {
  roomid: number;
  firstname: string;
  lastname: string;
  depositpaid: boolean;
  bookingdates: BookingDates;
}

export interface Booking extends UpdateBookingPayload {
  bookingid: number;
}

export interface BookingUpdateResponse {
  bookingid: number;
  booking: Booking;
}

export interface BookingListResponse {
  bookings: Booking[];
}

export interface HealthResponse {
  status: string;
  groups: string[];
}

export interface MethodNotAllowedResponse {
  timestamp: string;
  status: number;
  error: string;
  path: string;
}

export interface InvalidBookingPayload {
  roomid?: number;
  firstname?: string;
  lastname?: string;
  depositpaid?: boolean;
  email?: string;
  phone?: string;
  bookingdates?: Partial<BookingDates>;
}

export interface BrandingResponse {
  name: string;
  map: {
    latitude: number;
    longitude: number;
  };
  contact: {
    name: string;
    address?: string;
    phone: string;
    email: string;
  };
}

export interface Message {
  id: number;
  name: string;
  subject: string;
  read: boolean;
}

export interface MessagesResponse {
  messages: Message[];
}
