# Selenium Web Tests

Selenium WebDriver + Java UI tests for the public Automation in Testing / Shady Meadows B&B demo site.

## Coverage Snapshot

- 5 UI test cases
- 5 browser flows: booking availability, booking creation entry flow, booking cancel flow, room details navigation, and contact message submission

## Stack
- Java
- Selenium WebDriver
- JUnit 5
- Gradle

## Run
From the repository root:

```bash
./gradlew :selenium-web-tests:test
```

Set `AUTOMATION_IN_TESTING_BASE_URL` to target another deployment. The tests run headless by default; set `SELENIUM_HEADLESS=false` to open a visible browser.

## Portfolio runner
This module is included in:

```bash
./gradlew javaTest
./gradlew portfolioTest
./gradlew clean build
```
