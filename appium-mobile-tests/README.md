# Appium Mobile Tests

Gradle module reserved for Appium-based mobile automation examples.

## Stack
- Java
- Appium
- JUnit 5
- Gradle

## Current status
This module is wired into the root Gradle build, but it currently contains only a placeholder Java class and no executable Appium tests yet.

## Run
From the repository root:

```bash
./gradlew :appium-mobile-tests:test
```

This currently completes with `NO-SOURCE` until test classes are added under `src/test/java`.

## Portfolio runner
This module is included in:

```bash
./gradlew javaTest
./gradlew portfolioTest
./gradlew clean build
```
