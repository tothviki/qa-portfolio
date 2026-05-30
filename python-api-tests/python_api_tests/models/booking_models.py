from typing import NotRequired, TypedDict


class AuthCredentials(TypedDict):
    username: str
    password: str


class AuthTokenResponse(TypedDict):
    token: str


class ErrorResponse(TypedDict):
    error: str


class ValidationErrorsResponse(TypedDict):
    errors: list[str]


class BookingDates(TypedDict):
    checkin: str
    checkout: str


class PartialBookingDates(TypedDict, total=False):
    checkin: str
    checkout: str


class Room(TypedDict):
    roomid: int
    roomName: str
    type: str
    accessible: bool
    description: str
    features: list[str]
    image: str
    roomPrice: int


class RoomsResponse(TypedDict):
    rooms: list[Room]


class CreateBookingPayload(TypedDict):
    roomid: int
    firstname: str
    lastname: str
    depositpaid: bool
    email: str
    phone: str
    bookingdates: BookingDates
    totalprice: NotRequired[int]


class UpdateBookingPayload(TypedDict):
    roomid: int
    firstname: str
    lastname: str
    depositpaid: bool
    bookingdates: BookingDates


class Booking(UpdateBookingPayload):
    bookingid: int


class BookingUpdateResponse(TypedDict):
    bookingid: int
    booking: Booking


class BookingListResponse(TypedDict):
    bookings: list[Booking]


class HealthResponse(TypedDict):
    status: str
    groups: list[str]


class MethodNotAllowedResponse(TypedDict):
    timestamp: str
    status: int
    error: str
    path: str


class InvalidBookingPayload(TypedDict, total=False):
    roomid: int
    firstname: str
    lastname: str
    depositpaid: bool
    email: str
    phone: str
    bookingdates: PartialBookingDates


class BrandingContact(TypedDict, total=False):
    name: str
    address: str
    phone: str
    email: str


class BrandingMap(TypedDict):
    latitude: float
    longitude: float


class BrandingResponse(TypedDict):
    name: str
    map: BrandingMap
    contact: BrandingContact


class Message(TypedDict):
    id: int
    name: str
    subject: str
    read: bool


class MessagesResponse(TypedDict):
    messages: list[Message]

