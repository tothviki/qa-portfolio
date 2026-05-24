# REST Assured API Tests

REST Assured API suite for the public Automation in Testing service.

## Coverage Snapshot

- 20 TestNG tests
- 6 coverage areas: authentication, booking create, booking read, booking update, booking delete, and public API checks
- HTML report: `build/reports/tests/test/index.html`
- XML results: `build/test-results/test/`

## Stack
- Java
- REST Assured
- TestNG
- Gradle

## Project Scope

This module demonstrates API automation against `https://automationintesting.online` and is structured to show:

- request and response validation with REST Assured
- authenticated and unauthenticated API flows
- positive, negative, and edge-case coverage
- reusable clients, payload factories, and shared setup
- TestNG grouping for smoke and regression execution

## Scenario Coverage

| Area | Examples |
| --- | --- |
| Authentication | create token, reject invalid credentials |
| Booking create | valid booking, punctuation, unknown room id, validation errors, malformed JSON |
| Booking read | fetch by id, list by room id, auth required, missing room filter |
| Booking update | full update, unsupported patch method |
| Booking delete | delete existing booking, reject invalid token |
| Public API | health, rooms, branding, messages |

## Run
From the repository root:

```bash
./gradlew :rest-assured-api-tests:test
```

Run grouped suites:

```bash
./gradlew :rest-assured-api-tests:runSmokeTests
./gradlew :rest-assured-api-tests:runRegressionTests
```

## Layout

```text
rest-assured-api-tests/
|-- src/test/java/com/portfolio/base/       # shared setup and cleanup
|-- src/test/java/com/portfolio/clients/    # API request helpers
|-- src/test/java/com/portfolio/models/     # request and response records grouped by API area
|   |-- auth/
|   |-- booking/
|   |-- common/
|   `-- site/
|-- src/test/java/com/portfolio/payloads/   # test data factory
`-- src/test/java/com/portfolio/tests/      # TestNG scenarios
```

## Reporting

Gradle writes the HTML report to `rest-assured-api-tests/build/reports/tests/test/index.html`.
The XML results are written to `rest-assured-api-tests/build/test-results/test/`.

## Environment

Set `AUTOMATION_IN_TESTING_BASE_URL` to point at another deployment if needed.
