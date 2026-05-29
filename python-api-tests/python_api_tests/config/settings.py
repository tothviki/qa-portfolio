from dataclasses import dataclass
import os


@dataclass(frozen=True)
class Settings:
    base_url: str
    username: str
    password: str
    run_id: str
    worker_index: int
    request_timeout_seconds: float

    @classmethod
    def from_env(cls) -> "Settings":
        request_timeout_seconds = float(os.getenv("AUTOMATION_IN_TESTING_TIMEOUT_SECONDS", "10"))
        if request_timeout_seconds <= 0:
            raise ValueError("AUTOMATION_IN_TESTING_TIMEOUT_SECONDS must be greater than zero")

        base_url = os.getenv("AUTOMATION_IN_TESTING_BASE_URL", "https://automationintesting.online").rstrip("/")
        if not base_url:
            raise ValueError("AUTOMATION_IN_TESTING_BASE_URL must not be empty")

        return cls(
            base_url=base_url,
            username=os.getenv("AUTOMATION_IN_TESTING_USERNAME", "admin"),
            password=os.getenv("AUTOMATION_IN_TESTING_PASSWORD", "password"),
            run_id=os.getenv("TEST_RUN_ID", ""),
            worker_index=int(os.getenv("TEST_WORKER_INDEX", "0")),
            request_timeout_seconds=request_timeout_seconds,
        )


def get_settings() -> Settings:
    return Settings.from_env()

