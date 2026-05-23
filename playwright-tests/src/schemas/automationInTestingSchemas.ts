import { z } from 'zod';

const isoDatePattern = /^\d{4}-\d{2}-\d{2}$/;

export const authTokenSchema = z.object({
  token: z.string().min(1),
});

export const errorResponseSchema = z.object({
  error: z.string().min(1),
});

export const validationErrorsSchema = z.object({
  errors: z.array(z.string().min(1)),
});

export const roomSchema = z.object({
  roomid: z.number(),
  roomName: z.string().min(1),
  type: z.string().min(1),
  accessible: z.boolean(),
  description: z.string().min(1),
  features: z.array(z.string().min(1)),
  image: z.string().min(1),
  roomPrice: z.number(),
});

export const roomsResponseSchema = z.object({
  rooms: z.array(roomSchema),
});

export const bookingDatesSchema = z.object({
  checkin: z.string().regex(isoDatePattern),
  checkout: z.string().regex(isoDatePattern),
});

export const bookingSchema = z.object({
  bookingid: z.number(),
  roomid: z.number(),
  firstname: z.string().min(1),
  lastname: z.string().min(1),
  depositpaid: z.boolean(),
  bookingdates: bookingDatesSchema,
});

export const bookingListResponseSchema = z.object({
  bookings: z.array(bookingSchema),
});

export const bookingUpdateResponseSchema = z.object({
  bookingid: z.number(),
  booking: bookingSchema,
});

export const healthResponseSchema = z.object({
  status: z.string().min(1),
  groups: z.array(z.string().min(1)),
});

export const methodNotAllowedSchema = z.object({
  timestamp: z.string().min(1),
  status: z.number(),
  error: z.string().min(1),
  path: z.string().min(1),
});

export const brandingResponseSchema = z.object({
  name: z.string().min(1),
  map: z.object({
    latitude: z.number(),
    longitude: z.number(),
  }),
  contact: z.object({
    name: z.string().min(1),
    address: z.string().min(1).optional(),
    phone: z.string().min(1),
    email: z.string().min(1),
  }),
});

export const messagesResponseSchema = z.object({
  messages: z.array(
    z.object({
      id: z.number(),
      name: z.string().min(1),
      subject: z.string().min(1),
      read: z.boolean(),
    }),
  ),
});

export type AuthTokenContract = z.infer<typeof authTokenSchema>;
export type BookingContract = z.infer<typeof bookingSchema>;
export type BookingUpdateContract = z.infer<typeof bookingUpdateResponseSchema>;
