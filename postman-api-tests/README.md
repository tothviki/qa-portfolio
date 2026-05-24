# Postman API Tests

Postman/Newman API automation demo for the public Automation in Testing service.

## Coverage Snapshot

- 3 focused collections covering 24 unique checks across auth, booking CRUD, public API, and edge cases
- 2 execution environments: production and local
- JSON reports: `build/reports/postman/*-newman-report.json`

## Stack
- Focused Postman collections
- Newman
- Gradle wrapper tasks for portfolio-level execution

## What it covers
- Authentication
- Booking create, read, update, partial update, and delete requests
- Room discovery, branding, message, and health checks
- Negative checks aligned with the Playwright API suite
- Environment-specific execution
- JSON report output under the module build directory

## Reviewer Path
Start with these files:
1. `collections/booking-api-tests.postman_collection.json`
2. `collections/authentication-api-tests.postman_collection.json`
3. `collections/public-api-tests.postman_collection.json`
4. `environments/production.postman_environment.json`
5. `build.gradle`

## Run
From the repository root:

```bash
./gradlew :postman-api-tests:postmanTest
```

Run with the local Postman environment:

```bash
./gradlew :postman-api-tests:postmanLocalTest
```

Select an environment by property:

```bash
./gradlew :postman-api-tests:postmanTest -PpostmanEnvironment=production
./gradlew :postman-api-tests:postmanTest -PpostmanEnvironment=local
```

Run a focused collection:

```bash
./gradlew :postman-api-tests:postmanAuthenticationTest
./gradlew :postman-api-tests:postmanBookingTest
./gradlew :postman-api-tests:postmanPublicApiTest
```

## Files
- `collections/authentication-api-tests.postman_collection.json`
- `collections/booking-api-tests.postman_collection.json`
- `collections/public-api-tests.postman_collection.json`
- `environments/production.postman_environment.json`
- `environments/local.postman_environment.json`
