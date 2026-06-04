from __future__ import annotations

import json
from typing import Any

import pytest
import requests

from python_api_tests.clients.booking_api_client import BookingApiClient
from python_api_tests.config.settings import Settings


def _settings() -> Settings:
    return Settings(
        base_url="https://example.test",
        username="admin",
        password="password",
        run_id="unit",
        worker_index=0,
        request_timeout_seconds=3.5,
    )


def _response(status_code: int, body: dict[str, Any] | None = None) -> requests.Response:
    response = requests.Response()
    response.status_code = status_code
    response._content = json.dumps(body or {}).encode("utf-8")
    response.headers["Content-Type"] = "application/json"
    return response


class FakeSession(requests.Session):
    def __init__(self, responses: list[requests.Response]) -> None:
        super().__init__()
        self.responses = responses
        self.calls: list[dict[str, Any]] = []

    def request(self, method: str, url: str, **kwargs: Any) -> requests.Response:
        kwargs["method"] = method
        kwargs["url"] = url
        self.calls.append(kwargs)
        response = self.responses.pop(0)
        if isinstance(response, Exception):
            raise response
        return response


def test_client_applies_base_url_default_timeout_and_uppercase_method() -> None:
    session = FakeSession([_response(200)])
    client = BookingApiClient(settings=_settings(), session=session)

    client.get_rooms()

    assert session.calls == [
        {
            "method": "GET",
            "url": "https://example.test/api/room",
            "timeout": 3.5,
        }
    ]


def test_authenticate_caches_token_after_successful_login() -> None:
    session = FakeSession([_response(200, {"token": "cached-token"})])
    client = BookingApiClient(settings=_settings(), session=session)

    first_token = client.authenticate()
    second_token = client.authenticate()

    assert first_token == "cached-token"
    assert second_token == "cached-token"
    assert len(session.calls) == 1
    assert session.calls[0]["json"] == {"username": "admin", "password": "password"}


def test_authenticate_raises_when_login_fails() -> None:
    session = FakeSession([_response(401, {"error": "Invalid credentials"})])
    client = BookingApiClient(settings=_settings(), session=session)

    with pytest.raises(RuntimeError, match="Authentication failed with status 401"):
        client.authenticate()


def test_authenticate_retries_once_after_read_timeout() -> None:
    session = FakeSession(
        [
            requests.ReadTimeout("timed out"),
            _response(200, {"token": "retried-token"}),
        ]
    )
    client = BookingApiClient(settings=_settings(), session=session)

    token = client.authenticate()

    assert token == "retried-token"
    assert len(session.calls) == 2


def test_delete_tracked_bookings_removes_successful_cleanup_ids() -> None:
    session = FakeSession(
        [
            _response(200, {"token": "cleanup-token"}),
            _response(202),
            _response(403, {"error": "Forbidden"}),
        ]
    )
    client = BookingApiClient(settings=_settings(), session=session)
    client.track_booking(10)
    client.track_booking(11)

    result = client.delete_tracked_bookings()

    assert result.deleted_booking_ids == [11]
    assert len(result.errors) == 1
    assert result.errors[0].booking_id == 10
    assert "HTTP 403" in result.errors[0].message
