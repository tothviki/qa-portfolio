# JMeter Performance Tests

Gradle module reserved for JMeter performance testing examples.

## Stack
- JMeter
- Java/Gradle module wrapper
- Gradle portfolio runner

## Current status
This module is wired into the root Gradle build, but it currently contains only a placeholder Java class and no executable JMeter test plan integration yet.

## Run
From the repository root:

```bash
./gradlew :jmeter-performance-tests:test
```

This currently completes with `NO-SOURCE` until test execution is added.

## Portfolio runner
This module is included in:

```bash
./gradlew javaTest
./gradlew portfolioTest
./gradlew clean build
```
