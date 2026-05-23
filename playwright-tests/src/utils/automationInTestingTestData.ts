import {
  BookingDates,
  CreateBookingPayload,
  InvalidBookingPayload,
  UpdateBookingPayload,
} from '../models/automationInTesting';

const RUN_ID = process.env.TEST_RUN_ID ?? Date.now().toString(36);
const WORKER_OFFSET = Number(process.env.TEST_WORKER_INDEX ?? '0') * 20;
let sequence = 0;

function nextSequence(): number {
  sequence += 1;
  return sequence;
}

function pad(value: number): string {
  return value.toString().padStart(2, '0');
}

export class AutomationInTestingTestData {
  static runId(): string {
    return RUN_ID;
  }

  static nextId(prefix = 'pw'): string {
    return `${prefix}-${RUN_ID}-${pad(nextSequence())}`;
  }

  static isoDateFromToday(offsetDays: number): string {
    const today = new Date();
    const utcDate = new Date(Date.UTC(today.getUTCFullYear(), today.getUTCMonth(), today.getUTCDate() + offsetDays));
    return utcDate.toISOString().split('T')[0];
  }

  static futureDates(startDayOffset = WORKER_OFFSET + nextSequence() + 1, stayLength = 2): BookingDates {
    return {
      checkin: this.isoDateFromToday(startDayOffset),
      checkout: this.isoDateFromToday(startDayOffset + stayLength),
    };
  }

  static randomDates(): BookingDates {
    return this.futureDates();
  }

  static randomBooking(roomid = 1): CreateBookingPayload {
    const id = this.nextId();
    const shortId = `${RUN_ID.slice(-4)}${pad(nextSequence())}`;
    return {
      roomid,
      firstname: `F${shortId}`,
      lastname: `L${shortId}`,
      depositpaid: nextSequence() % 2 === 0,
      email: `${id}@example.com`,
      phone: `07${pad(nextSequence()).repeat(5).slice(0, 9)}`,
      bookingdates: this.futureDates(),
    };
  }

  static updatedBooking(roomid = 1): UpdateBookingPayload {
    return {
      roomid,
      firstname: 'UpdatedFirst',
      lastname: 'UpdatedLast',
      depositpaid: false,
      bookingdates: this.futureDates(),
    };
  }

  static bookingWithSupportedCharacters(roomid = 1): CreateBookingPayload {
    return {
      roomid,
      firstname: 'Anne-Marie',
      lastname: 'OConnor-Smith',
      depositpaid: true,
      email: `anne.marie-${this.nextId('chars')}@example.com`,
      phone: '07123456789',
      bookingdates: this.futureDates(),
    };
  }

  static namedBooking(firstname: string, lastname: string, roomid = 1): CreateBookingPayload {
    const id = this.nextId('named');
    return {
      roomid,
      firstname,
      lastname,
      depositpaid: true,
      email: `${firstname}.${lastname}.${id}@example.com`.toLowerCase(),
      phone: `07${pad(nextSequence()).repeat(5).slice(0, 9)}`,
      bookingdates: this.futureDates(),
    };
  }

  static uniqueFirstname(prefix = 'FilterTest'): string {
    return `${prefix}${this.nextId('name').replace(/-/g, '')}`;
  }

  static invalidBookingPayload(): InvalidBookingPayload {
    return {
      roomid: 1,
      firstname: 'Al',
      lastname: '',
      depositpaid: true,
      email: 'not-an-email',
      phone: '123',
      bookingdates: {
        checkin: '',
        checkout: '',
      },
    };
  }
}
