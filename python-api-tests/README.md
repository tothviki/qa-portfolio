# Python API Tests

Python + pytest API tests for the public Automation in Testing / Shady Meadows B&B demo service.

This module mirrors the Playwright API coverage with a Python implementation. It currently contains
33 tests covering:

- smoke tests
- CRUD flow tests
- negative tests
- validation tests
- schema and contract tests
- parametrized tests
- unit tests for client behavior and test-data generation

## Stack

- Python 3.11+
- pytest
- requests
- jsonschema
- ruff
- mypy

## Layout

```text
python-api-tests/
|-- README.md
|-- pyproject.toml
|-- pytest.ini
|-- requirements.txt
|-- requirements-dev.txt
|-- reports/
|-- schemas/
|   `-- booking_schema.json
|-- python_api_tests/
|   |-- clients/
|   |   `-- booking_api_client.py
|   |-- config/
|   |   `-- settings.py
|   |-- models/
|   |   `-- booking_models.py
|   `-- utils/
|       |-- assertions.py
|       `-- data_factory.py
`-- tests/
    |-- test_auth.py
    |-- test_booking_api_client_unit.py
    |-- test_booking_crud.py
    |-- test_contract_validation.py
    |-- test_data_factory_unit.py
    `-- test_negative_cases.py
```

## Setup

The root Gradle tasks `./gradlew pythonTest` and `./gradlew pythonQuality` will create and refresh `python-api-tests/.venv` automatically.

If you want to run `pytest` directly from `python-api-tests/`, set up the virtual environment manually:

```bash
python -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
pip install -r requirements-dev.txt
```

## Run

From the repository root:

```bash
./gradlew pythonTest
./gradlew pythonQuality
```

From `python-api-tests/`:

```bash
pytest
pytest -m smoke
pytest -m crud
pytest -m negative
pytest -m validation
pytest -m contract
```

`./gradlew pythonTest` is the preferred repository-level command because it uses the same virtual
environment, requirements, pytest configuration, and JUnit report path as CI.

## Environment Variables

- `AUTOMATION_IN_TESTING_BASE_URL`: base URL for the demo service, defaults to `https://automationintesting.online`
- `AUTOMATION_IN_TESTING_USERNAME`: API auth username, defaults to `admin`
- `AUTOMATION_IN_TESTING_PASSWORD`: API auth password, defaults to `password`
- `AUTOMATION_IN_TESTING_TIMEOUT_SECONDS`: request timeout in seconds, defaults to `10`
- `TEST_RUN_ID`: overrides the run-scoped test-data prefix
- `TEST_WORKER_INDEX`: helps distribute unique generated data across workers

## Reports

`./gradlew pythonTest` writes the pytest JUnit report to:

```text
python-api-tests/reports/junit.xml
```

## Design Notes

- The integration tests run against a public demo service, so failures can still happen if that
  service changes behavior or is temporarily unavailable.
- Test data uses run-scoped identifiers and future booking dates to reduce collisions in shared
  environments.
- The mocked unit tests cover client request construction, auth-token caching, cleanup behavior, and
  data-factory uniqueness without calling the live service.
