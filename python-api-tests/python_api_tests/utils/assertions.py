from __future__ import annotations

import json
from pathlib import Path
from typing import Any

from requests import Response


SCHEMA_DIR = Path(__file__).resolve().parents[2] / "schemas"


def assert_json_response(response: Response) -> None:
    content_type = response.headers.get("content-type", "")
    assert "json" in content_type.lower(), f"Expected JSON response, got {content_type!r}"


def load_schema(schema_name: str) -> dict[str, Any]:
    schema_path = SCHEMA_DIR / schema_name
    return json.loads(schema_path.read_text(encoding="utf-8"))

