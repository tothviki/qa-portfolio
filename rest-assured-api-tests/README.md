# REST Assured API Tests

API automation demo for the public Restful Booker service.

## Stack
- Java
- REST Assured
- TestNG
- Gradle

## What it covers
- Authentication token creation
- Booking create, read, update, and delete flow
- Request helper/client classes
- Test data factory
- Smoke and regression task entry points

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

## API documentation
https://restful-booker.herokuapp.com/apidoc/index.html
