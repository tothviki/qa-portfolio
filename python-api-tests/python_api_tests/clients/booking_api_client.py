from __future__ import annotations

from dataclasses import dataclass, field
from typing import Optional

import requests
from requests import ReadTimeout

from python_api_tests.config.settings import Settings, get_settings
from python_api_tests.models.booking_models import (
    AuthCredentials,
    CreateBookingPayload,
    InvalidBookingPayload,
    UpdateBookingPayload,
)


@dataclass
class BookingCleanupError:
    booking_id: int | None = None
    message: str = ""


@dataclass
class BookingCleanupResult:
    deleted_booking_ids: list[int] = field(default_factory=list)
    errors: list[BookingCleanupError] = field(default_factory=list)


class BookingApiClient:
    AUTH_REQUEST_ATTEMPTS = 2

    def __init__(
        self,
        settings: Settings | None = None,
        session: requests.Session | None = None,
    ) -> None:
        self.settings = settings or get_settings()
        self.session = session or requests.Session()
        self.session.headers.setdefault("Accept", "application/json")
        self._tracked_booking_ids: set[int] = set()
        self._auth_token: Optional[str] = None

    def _url(self, path: str) -> str:
        return f"{self.settings.base_url}{path}"

    def _request(self, method: str, path: str, **kwargs) -> requests.Response:
        kwargs.setdefault("timeout", self.settings.request_timeout_seconds)
        return self.session.request(method=method.upper(), url=self._url(path), **kwargs)

    def create_token(self, credentials: AuthCredentials | None = None) -> requests.Response:
        payload = credentials or {
            "username": self.settings.username,
            "password": self.settings.password,
        }
        last_error: ReadTimeout | None = None

        for _ in range(self.AUTH_REQUEST_ATTEMPTS):
            try:
                return self._request("POST", "/api/auth/login", json=payload)
            except ReadTimeout as exc:
                last_error = exc

        assert last_error is not None
        raise last_error

    def authenticate(self) -> str:
        if self._auth_token:
            return self._auth_token

        response = self.create_token()
        if response.status_code != 200:
            raise RuntimeError(f"Authentication failed with status {response.status_code}")

        token = response.json()["token"]
        self._auth_token = token
        return token

    def create_booking(self, booking_data: CreateBookingPayload) -> requests.Response:
        return self.post_booking_payload(booking_data)

    def post_booking_payload(
        self,
        payload: CreateBookingPayload | InvalidBookingPayload,
    ) -> requests.Response:
        return self._request(
            "POST",
            "/api/booking",
            json=payload,
            headers={"Content-Type": "application/json"},
        )

    def post_raw_booking_payload(
        self,
        body: str,
        content_type: str = "application/json",
    ) -> requests.Response:
        return self._request(
            "POST",
            "/api/booking",
            data=body,
            headers={"Content-Type": content_type},
        )

    def track_booking(self, booking_id: int) -> None:
        self._tracked_booking_ids.add(booking_id)

    def untrack_booking(self, booking_id: int) -> None:
        self._tracked_booking_ids.discard(booking_id)

    def delete_tracked_bookings(self) -> BookingCleanupResult:
        result = BookingCleanupResult()
        if not self._tracked_booking_ids:
            return result

        try:
            auth_token = self.authenticate()
        except Exception as exc:  # pragma: no cover - cleanup safety
            result.errors.append(
                BookingCleanupError(message=f"Could not authenticate for cleanup: {exc}")
            )
            return result

        for booking_id in sorted(self._tracked_booking_ids, reverse=True):
            try:
                response = self.delete_booking(booking_id, auth_token)
                if response.ok:
                    result.deleted_booking_ids.append(booking_id)
                    self.untrack_booking(booking_id)
                    continue

                result.errors.append(
                    BookingCleanupError(
                        booking_id=booking_id,
                        message=f"Delete returned HTTP {response.status_code}: {response.text}",
                    )
                )
            except Exception as exc:  # pragma: no cover - cleanup safety
                result.errors.append(BookingCleanupError(booking_id=booking_id, message=str(exc)))

        return result

    def get_rooms(self) -> requests.Response:
        return self._request("GET", "/api/room")

    def get_booking(self, booking_id: int, auth_token: str) -> requests.Response:
        return self._request(
            "GET",
            f"/api/booking/{booking_id}",
            headers={"Cookie": f"token={auth_token}"},
        )

    def get_bookings_by_room(self, room_id: int, auth_token: str) -> requests.Response:
        return self._request(
            "GET",
            "/api/booking",
            params={"roomid": room_id},
            headers={"Cookie": f"token={auth_token}"},
        )

    def get_bookings_without_room(self, auth_token: str) -> requests.Response:
        return self._request(
            "GET",
            "/api/booking",
            headers={"Cookie": f"token={auth_token}"},
        )

    def get_bookings_without_auth(self, room_id: int) -> requests.Response:
        return self._request("GET", "/api/booking", params={"roomid": room_id})

    def update_booking(
        self,
        booking_id: int,
        booking_data: UpdateBookingPayload,
        auth_token: str,
    ) -> requests.Response:
        return self._request(
            "PUT",
            f"/api/booking/{booking_id}",
            json=booking_data,
            headers={"Cookie": f"token={auth_token}", "Content-Type": "application/json"},
        )

    def patch_booking(
        self,
        booking_id: int,
        partial_data: dict[str, object],
        auth_token: str,
    ) -> requests.Response:
        return self._request(
            "PATCH",
            f"/api/booking/{booking_id}",
            json=partial_data,
            headers={"Cookie": f"token={auth_token}", "Content-Type": "application/json"},
        )

    def delete_booking(self, booking_id: int, auth_token: str) -> requests.Response:
        return self._request(
            "DELETE",
            f"/api/booking/{booking_id}",
            headers={"Cookie": f"token={auth_token}"},
        )

    def health_check(self) -> requests.Response:
        return self._request("GET", "/api/booking/actuator/health")

    def get_branding(self) -> requests.Response:
        return self._request("GET", "/api/branding")

    def get_messages(self) -> requests.Response:
        return self._request("GET", "/api/message")
