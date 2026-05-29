import pytest

from python_api_tests.models.booking_models import AuthCredentials
from python_api_tests.utils.assertions import assert_json_response


@pytest.mark.smoke
def test_smoke_post_auth_login_returns_token(api_client) -> None:
    response = api_client.create_token()

    assert response.status_code == 200
    assert_json_response(response)
    body = response.json()
    assert isinstance(body["token"], str)
    assert body["token"]


@pytest.mark.parametrize(
    ("credentials", "expected_status", "expected_error"),
    [
        ({"username": "admin", "password": "wrong-password"}, 401, "Invalid credentials"),
        ({"username": "wrong-user", "password": "password"}, 401, "Invalid credentials"),
    ],
)
def test_invalid_credentials_are_rejected(
    api_client,
    credentials: AuthCredentials,
    expected_status: int,
    expected_error: str,
) -> None:
    response = api_client.create_token(credentials)

    assert response.status_code == expected_status
    assert_json_response(response)
    assert response.json()["error"] == expected_error
