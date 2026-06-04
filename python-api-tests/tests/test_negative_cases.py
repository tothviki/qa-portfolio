import pytest

from python_api_tests.models.booking_models import Booking
from python_api_tests.utils.assertions import assert_json_response
from python_api_tests.utils.data_factory import DataFactory


def _create_booking_for_patch_test(api_client):
    last_response = None
    for room_id in (1, 2, 3):
        response = api_client.create_booking(DataFactory.random_booking(room_id))
        if response.status_code == 201:
            return response
        last_response = response

    assert last_response is not None
    pytest.fail(f"Could not create booking for PATCH test. Last status: {last_response.status_code}")


@pytest.mark.negative
def test_checkout_before_checkin_returns_conflict(api_client) -> None:
    response = api_client.create_booking(
        {
            **DataFactory.random_booking(),
            "bookingdates": {
                "checkin": "2028-01-12",
                "checkout": "2028-01-10",
            },
        }
    )

    assert response.status_code == 409
    assert_json_response(response)
    assert response.json()["error"] == "Failed to create booking"


@pytest.mark.negative
def test_delete_booking_requires_valid_authentication(api_client) -> None:
    create_response = api_client.create_booking(DataFactory.random_booking())
    assert create_response.status_code == 201
    created: Booking = create_response.json()
    api_client.track_booking(created["bookingid"])

    response = api_client.delete_booking(created["bookingid"], "not-a-real-token")

    assert response.status_code == 403


@pytest.mark.negative
def test_get_bookings_requires_authentication(api_client) -> None:
    response = api_client.get_bookings_without_auth(1)

    assert response.status_code == 401
    assert_json_response(response)
    assert response.json()["error"] == "Authentication required"


@pytest.mark.negative
def test_get_bookings_requires_room_id_filter(api_client) -> None:
    auth_token = api_client.authenticate()

    response = api_client.get_bookings_without_room(auth_token)

    assert response.status_code == 400
    assert_json_response(response)
    assert response.json()["error"] == "Room ID is required"


@pytest.mark.negative
def test_patch_booking_documents_unsupported_method(api_client) -> None:
    create_response = _create_booking_for_patch_test(api_client)
    created: Booking = create_response.json()
    api_client.track_booking(created["bookingid"])
    auth_token = api_client.authenticate()

    response = api_client.patch_booking(created["bookingid"], {"firstname": "PatchedFirst"}, auth_token)

    assert response.status_code == 405
    assert_json_response(response)
    assert response.json()["error"] == "Method Not Allowed"


@pytest.mark.negative
def test_unknown_room_id_currently_creates_booking_as_documented_product_behavior(api_client) -> None:
    response = api_client.create_booking(DataFactory.random_booking(9999))

    assert response.status_code == 201
    assert_json_response(response)
    created: Booking = response.json()
    api_client.track_booking(created["bookingid"])
    assert created["roomid"] == 9999


@pytest.mark.negative
def test_malformed_json_returns_server_error(api_client) -> None:
    response = api_client.post_raw_booking_payload("{roomid:")

    assert response.status_code == 500


@pytest.mark.validation
@pytest.mark.parametrize("missing_field", ["firstname", "lastname"])
def test_missing_required_guest_name_fields_return_validation_errors(api_client, missing_field: str) -> None:
    payload = DataFactory.random_booking()
    payload.pop(missing_field)

    response = api_client.post_booking_payload(payload)

    assert response.status_code == 400
    assert_json_response(response)
    body = response.json()
    assert len(body["errors"]) > 0


@pytest.mark.validation
def test_invalid_booking_payload_returns_validation_errors(api_client) -> None:
    response = api_client.post_booking_payload(DataFactory.invalid_booking_payload())

    assert response.status_code == 400
    assert_json_response(response)
    body = response.json()
    assert len(body["errors"]) > 0
