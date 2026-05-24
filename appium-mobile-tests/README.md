# Appium Mobile Tests

Gradle module reserved for Appium-based mobile web automation examples against
the public Automation in Testing site.

## Stack
- Java
- Appium Java Client
- JUnit 5
- Gradle

## Current status
This module is wired into the root Gradle build and contains 3 executable Appium tests against the public `automationintesting.online` site in Android Chrome.

The code is organized with page objects so the test classes stay thin and the
locators live in one place.

## Run
From the repository root:

```bash
./gradlew :appium-mobile-tests:test
```

The Appium tests in this module are opt-in. To run them against a live Android emulator or device with Chrome installed, set:

```bash
RUN_APPIUM_TESTS=true
APPIUM_SERVER_URL=http://127.0.0.1:4723
MOBILE_BASE_URL=https://automationintesting.online
ANDROID_UDID=<optional-device-udid>
```

These tests do not require a custom mobile app. They open the public website in a real mobile browser session and verify a few core user journeys:

- landing on the home page
- reaching the booking section
- completing a booking search and opening the reservation page
- submitting an invalid contact form and seeing validation feedback

## Portfolio runner
This module is included in:

```bash
./gradlew javaTest
./gradlew portfolioTest
./gradlew clean build
```
