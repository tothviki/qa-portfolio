# Postman API Tests

Postman/Newman API automation demo for the public Automation in Testing service.

## Stack
- Postman collection
- Newman
- Gradle wrapper tasks for portfolio-level execution

## What it covers
- Authentication
- Booking create, read, update, partial update, and delete requests
- Room discovery, branding, message, and health checks
- Negative checks aligned with the Playwright API suite
- Environment-specific execution
- JSON report output under the module build directory

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

## Files
- `collections/Restful-Booker-API-Tests.json`
- `environments/production.postman_environment.json`
- `environments/local.postman_environment.json`
