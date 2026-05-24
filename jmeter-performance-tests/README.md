# JMeter Performance Tests

Executable JMeter performance testing examples.

This module demonstrates basic performance-test wiring through Gradle.

## Stack
- JMeter
- Java/Gradle
- Gradle portfolio runner

## Current Status
This module is wired into the root Gradle build and now includes executable JMeter-based performance tests:
- 2 executable scenarios
- 14 total samples across both scenarios:
  - `POST /api/auth/login`: 6 samples
  - `GET /api/room`: 8 samples

## Scenarios
- `POST /api/auth/login` submission load test
- `GET /api/room` concurrent room catalog lookup test

## Run
From the repository root:

```bash
./gradlew :jmeter-performance-tests:test
```

This runs the JMeter-backed performance tests in `src/test/java`.

## Portfolio runner
This module is included in:

```bash
./gradlew javaTest
./gradlew additionalTest
./gradlew portfolioFullTest
```
