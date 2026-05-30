import pytest

from python_api_tests.models.booking_models import Booking
from python_api_tests.utils.assertions import assert_json_response
from python_api_tests.utils.data_factory import DataFactory


@pytest.mark.smoke
@pytest.mark.parametrize("room_id", [1, 2, 3])
def test_smoke_create_booking_for_supported_room_ids(api_client, room_id: int) -> None:
    booking_data = DataFactory.random_booking(room_id)
    response = api_client.create_booking(booking_data)

    assert response.status_code == 201
    assert_json_response(response)
    created = response.json()
    api_client.track_booking(created["bookingid"])
    assert created["roomid"] == room_id
    assert created["firstname"] == booking_data["firstname"]
    assert created["lastname"] == booking_data["lastname"]


@pytest.mark.crud
def test_crud_flow_create_read_update_delete_booking(api_client) -> None:
    booking_data = DataFactory.temporary_booking(1)

    create_response = api_client.create_booking(booking_data)
    assert create_response.status_code == 201
    assert_json_response(create_response)
    created: Booking = create_response.json()
    api_client.track_booking(created["bookingid"])

    auth_token = api_client.authenticate()

    get_response = api_client.get_booking(created["bookingid"], auth_token)
    assert get_response.status_code == 200
    assert_json_response(get_response)
    booking = get_response.json()
    assert booking["bookingid"] == created["bookingid"]
    assert booking["firstname"] == booking_data["firstname"]
    assert booking["lastname"] == booking_data["lastname"]

    updated_data = DataFactory.updated_booking(created["roomid"])
    update_response = api_client.update_booking(created["bookingid"], updated_data, auth_token)
    assert update_response.status_code == 200
    assert_json_response(update_response)
    updated = update_response.json()
    assert updated["bookingid"] == created["bookingid"]
    assert updated["booking"]["firstname"] == updated_data["firstname"]
    assert updated["booking"]["lastname"] == updated_data["lastname"]
    assert updated["booking"]["depositpaid"] is updated_data["depositpaid"]

    delete_response = api_client.delete_booking(created["bookingid"], auth_token)
    assert delete_response.status_code == 202
    api_client.untrack_booking(created["bookingid"])
