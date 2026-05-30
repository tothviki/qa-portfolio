import pytest
from jsonschema import validate

from python_api_tests.utils.assertions import load_schema
from python_api_tests.utils.data_factory import DataFactory


@pytest.mark.contract
def test_auth_token_response_contract(api_client) -> None:
    response = api_client.create_token()

    assert response.status_code == 200
    validate(instance=response.json(), schema=load_schema("auth_token_response_schema.json"))


@pytest.mark.contract
def test_invalid_auth_error_contract(api_client) -> None:
    response = api_client.create_token({"username": "admin", "password": "wrong-password"})

    assert response.status_code == 401
    validate(instance=response.json(), schema=load_schema("error_response_schema.json"))


@pytest.mark.contract
def test_validation_error_response_contract(api_client) -> None:
    response = api_client.post_booking_payload(DataFactory.invalid_booking_payload())

    assert response.status_code == 400
    validate(instance=response.json(), schema=load_schema("validation_errors_response_schema.json"))


@pytest.mark.contract
def test_booking_create_response_contract(api_client) -> None:
    response = api_client.create_booking(DataFactory.random_booking())

    assert response.status_code == 201
    created = response.json()
    api_client.track_booking(created["bookingid"])
    validate(instance=created, schema=load_schema("booking_schema.json"))


@pytest.mark.contract
def test_booking_read_response_contract(api_client) -> None:
    create_response = api_client.create_booking(DataFactory.random_booking())
    assert create_response.status_code == 201
    created = create_response.json()
    api_client.track_booking(created["bookingid"])
    auth_token = api_client.authenticate()

    response = api_client.get_booking(created["bookingid"], auth_token)

    assert response.status_code == 200
    validate(instance=response.json(), schema=load_schema("booking_schema.json"))


@pytest.mark.contract
def test_booking_update_response_contract(api_client) -> None:
    create_response = api_client.create_booking(DataFactory.temporary_booking())
    assert create_response.status_code == 201
    created = create_response.json()
    api_client.track_booking(created["bookingid"])
    auth_token = api_client.authenticate()

    updated_data = DataFactory.updated_booking(created["roomid"])

    response = api_client.update_booking(created["bookingid"], updated_data, auth_token)

    assert response.status_code == 200
    validate(instance=response.json(), schema=load_schema("booking_update_response_schema.json"))


@pytest.mark.contract
@pytest.mark.parametrize(
    ("endpoint_name", "request_method", "schema_name"),
    [
        ("health", "health_check", "health_response_schema.json"),
        ("rooms", "get_rooms", "rooms_response_schema.json"),
        ("branding", "get_branding", "branding_response_schema.json"),
        ("messages", "get_messages", "messages_response_schema.json"),
    ],
)
def test_public_endpoint_contracts(
    api_client,
    endpoint_name: str,
    request_method: str,
    schema_name: str,
) -> None:
    response = getattr(api_client, request_method)()

    assert response.status_code == 200, endpoint_name
    validate(instance=response.json(), schema=load_schema(schema_name))
