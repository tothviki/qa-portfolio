from datetime import datetime, timedelta, timezone
from itertools import count
import os

from python_api_tests.models.booking_models import (
    BookingDates,
    CreateBookingPayload,
    InvalidBookingPayload,
    UpdateBookingPayload,
)


_RUN_ID = os.getenv("TEST_RUN_ID") or datetime.now(timezone.utc).strftime("%Y%m%d%H%M%S")
_WORKER_OFFSET = int(os.getenv("TEST_WORKER_INDEX", "0")) * 20
_SEQUENCE = count(1)


def _next_sequence() -> int:
    return next(_SEQUENCE)


def _pad(value: int) -> str:
    return f"{value:02d}"


def _phone_number() -> str:
    suffix = _pad(_next_sequence()) * 5
    return f"07{suffix[:9]}"


class DataFactory:
    @staticmethod
    def run_id() -> str:
        return _RUN_ID

    @staticmethod
    def next_id(prefix: str = "py") -> str:
        return f"{prefix}-{_RUN_ID}-{_pad(_next_sequence())}"

    @staticmethod
    def iso_date_from_today(offset_days: int) -> str:
        today = datetime.now(timezone.utc).date()
        return (today + timedelta(days=offset_days)).isoformat()

    @staticmethod
    def future_dates(start_day_offset: int | None = None, stay_length: int = 2) -> BookingDates:
        if start_day_offset is None:
            start_day_offset = _WORKER_OFFSET + _next_sequence() + 1

        return {
            "checkin": DataFactory.iso_date_from_today(start_day_offset),
            "checkout": DataFactory.iso_date_from_today(start_day_offset + stay_length),
        }

    @staticmethod
    def random_dates() -> BookingDates:
        return DataFactory.future_dates()

    @staticmethod
    def random_booking(roomid: int = 1) -> CreateBookingPayload:
        identifier = DataFactory.next_id()
        short_id = f"{_RUN_ID[-4:]}{_pad(_next_sequence())}"
        return {
            "roomid": roomid,
            "firstname": f"F{short_id}",
            "lastname": f"L{short_id}",
            "depositpaid": _next_sequence() % 2 == 0,
            "email": f"{identifier}@example.com",
            "phone": _phone_number(),
            "bookingdates": DataFactory.future_dates(),
        }

    @staticmethod
    def updated_booking(roomid: int = 1) -> UpdateBookingPayload:
        short_id = f"{_RUN_ID[-4:]}{_pad(_next_sequence())}"
        return {
            "roomid": roomid,
            "firstname": f"Updated{short_id}",
            "lastname": f"Guest{short_id}",
            "depositpaid": False,
            "bookingdates": DataFactory.future_dates(),
        }

    @staticmethod
    def temporary_booking(roomid: int = 1) -> CreateBookingPayload:
        identifier = DataFactory.next_id("update")
        short_id = f"{_RUN_ID[-4:]}{_pad(_next_sequence())}"
        return {
            "roomid": roomid,
            "firstname": f"Temp{short_id}",
            "lastname": f"Guest{short_id}",
            "depositpaid": True,
            "email": f"temp.first.{identifier}@example.com",
            "phone": _phone_number(),
            "bookingdates": DataFactory.future_dates(),
        }

    @staticmethod
    def booking_with_supported_characters(roomid: int = 1) -> CreateBookingPayload:
        return {
            "roomid": roomid,
            "firstname": "Anne-Marie",
            "lastname": "OConnor-Smith",
            "depositpaid": True,
            "email": f"anne.marie-{DataFactory.next_id('chars')}@example.com",
            "phone": "07123456789",
            "bookingdates": DataFactory.future_dates(),
        }

    @staticmethod
    def named_booking(firstname: str, lastname: str, roomid: int = 1) -> CreateBookingPayload:
        identifier = DataFactory.next_id("named")
        return {
            "roomid": roomid,
            "firstname": firstname,
            "lastname": lastname,
            "depositpaid": True,
            "email": f"{firstname}.{lastname}.{identifier}@example.com".lower(),
            "phone": _phone_number(),
            "bookingdates": DataFactory.future_dates(),
        }

    @staticmethod
    def unique_firstname(prefix: str = "FilterTest") -> str:
        return f"{prefix}{DataFactory.next_id('name').replace('-', '')}"

    @staticmethod
    def invalid_booking_payload() -> InvalidBookingPayload:
        return {
            "roomid": 1,
            "firstname": "Al",
            "lastname": "",
            "depositpaid": True,
            "email": "not-an-email",
            "phone": "123",
            "bookingdates": {
                "checkin": "",
                "checkout": "",
            },
        }
