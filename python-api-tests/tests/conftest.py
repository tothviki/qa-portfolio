import warnings

import pytest

from python_api_tests.clients.booking_api_client import BookingApiClient


@pytest.fixture
def api_client() -> BookingApiClient:
    client = BookingApiClient()
    yield client

    cleanup = client.delete_tracked_bookings()
    if cleanup.errors:
        warnings.warn(f"Booking cleanup completed with errors: {cleanup}")
