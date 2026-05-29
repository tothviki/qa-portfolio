from datetime import datetime, timezone

from python_api_tests.utils.data_factory import DataFactory


def test_random_booking_generates_unique_guest_identity_and_future_dates() -> None:
    first = DataFactory.random_booking()
    second = DataFactory.random_booking()
    today = datetime.now(timezone.utc).date()

    assert first["email"] != second["email"]
    assert first["phone"] != second["phone"]
    assert datetime.fromisoformat(first["bookingdates"]["checkin"]).date() > today
    assert first["bookingdates"]["checkin"] < first["bookingdates"]["checkout"]


def test_temporary_and_updated_bookings_do_not_reuse_fixed_dates() -> None:
    temporary = DataFactory.temporary_booking()
    updated = DataFactory.updated_booking()

    assert temporary["firstname"].startswith("Temp")
    assert updated["firstname"].startswith("Updated")
    assert temporary["bookingdates"] != updated["bookingdates"]
