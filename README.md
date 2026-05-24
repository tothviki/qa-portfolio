# QA Automation Portfolio - Viktória Tóth

[![CI](https://github.com/tothviki/qa-portfolio/actions/workflows/ci.yml/badge.svg?branch=main)](https://github.com/tothviki/qa-portfolio/actions/workflows/ci.yml)
![Playwright](https://img.shields.io/badge/Framework-Playwright-2EAD33?style=flat&logo=playwright&logoColor=white)
![TypeScript](https://img.shields.io/badge/Language-TypeScript-3178C6?style=flat&logo=typescript&logoColor=white)
![Java](https://img.shields.io/badge/Language-Java-007396?style=flat&logo=openjdk&logoColor=white)

Senior QA Automation / SDET portfolio demonstrating API, web, mobile and
performance testing skills with TypeScript, Java, Playwright, REST Assured,
Postman/Newman, Selenium WebDriver, Appium and JMeter.

This repository contains focused and documented testing projects that show
practical QA automation skills, maintainable test structure, regression
thinking and multi-tool testing experience. The Playwright module is the most
complete example, supported by API-focused REST Assured and Postman examples,
plus additional examples for Selenium, Appium and JMeter.

## Portfolio Snapshot

| Area | Module | Coverage | Report |
| --- | --- | --- | --- |
| Main automation example | [playwright-tests](playwright-tests) | 36 test cases: 23 API, 12 UI, 1 E2E across 7 projects | `playwright-tests/test-reports/html` |
| API automation | [rest-assured-api-tests](rest-assured-api-tests) | 21 TestNG tests across auth, booking CRUD and public API checks | `rest-assured-api-tests/build/reports/tests/test/index.html` |
| API automation | [postman-api-tests](postman-api-tests) | 24 checks across auth, booking, public API and edge-case collections | `postman-api-tests/build/reports/postman/*-newman-report.json` |
| Supporting examples | [selenium-web-tests](selenium-web-tests), [appium-mobile-tests](appium-mobile-tests), [jmeter-performance-tests](jmeter-performance-tests) | Selenium: 5 UI tests; Appium: 3 mobile web tests; JMeter: 2 executable scenarios, 14 total samples | Module build reports |

## Portfolio Highlights

- Multi-tool QA portfolio covering API, web, mobile and performance testing
- Deeper Playwright + TypeScript example covering API, UI and E2E scenarios
- API automation shown through Playwright API, REST Assured and Postman/Newman
- Supporting examples for Selenium WebDriver, Appium and JMeter
- Repository-level Gradle tasks for repeatable portfolio validation
- Module-level commands for focused API, UI, mobile and performance test runs
- GitHub Actions workflow for automated CI validation

## What This Portfolio Demonstrates

- Current hands-on QA automation practice with modern Playwright + TypeScript
- API testing across multiple approaches: Playwright API, REST Assured and Postman/Newman
- Maintainable test design with fixtures, helpers, page objects and reusable test data
- Regression-oriented coverage across API, web, mobile and performance areas
- Gradle-based orchestration across multiple QA modules
- Practical documentation for technical review, local execution and CI validation

## Tech Stack

- **Programming:** TypeScript, Java, Python
- **Test Automation:** Playwright, REST Assured, Postman/Newman, Selenium WebDriver, Appium
- **Performance Testing:** JMeter
- **Build & Execution:** Gradle, npm
- **Quality & Tooling:** GitHub Actions, TestNG, PyTest, ESLint, Prettier
- **Testing Areas:** API, Web UI, Mobile, E2E, Performance, Regression

## Projects

### Playwright API, UI and E2E Automation

**Playwright + TypeScript**

Modern test automation suite for the public Automation in Testing / Shady
Meadows B&B demo application. Covers API, UI and E2E scenarios across booking,
room discovery, validation, health, contact form and cross-browser workflows.

Demonstrates maintainable Playwright test architecture with API helpers, page
objects, reusable fixtures, structured test data, cleanup logic, schema
validation and reporting.

→ [playwright-tests](playwright-tests)

### API Automation Examples

#### REST Assured API Tests

**REST Assured + Java**

Java-based API test suite demonstrating request and response validation,
reusable test structure and regression-oriented API checks.

→ [rest-assured-api-tests](rest-assured-api-tests)

#### Postman API Tests

**Postman + Newman**

Focused Postman collections with Newman command-line execution for API checks
that are easy to review, run locally and integrate into automated workflows.

→ [postman-api-tests](postman-api-tests)

### Additional Examples

#### Selenium Web Tests

**Selenium WebDriver + Java**

Web UI automation examples demonstrating browser-based testing structure and
Selenium WebDriver usage.

→ [selenium-web-tests](selenium-web-tests)

#### Appium Mobile Tests

**Appium + Java**

Mobile web automation examples demonstrating an Appium page-object structure for
Android Chrome against the public Automation in Testing site.

→ [appium-mobile-tests](appium-mobile-tests)

#### JMeter Performance Tests

**JMeter + Gradle**

Performance testing examples integrated with Gradle for repeatable execution.

→ [jmeter-performance-tests](jmeter-performance-tests)

## Repository Structure

```text
qa-portfolio/
|-- appium-mobile-tests/
|-- jmeter-performance-tests/
|-- playwright-tests/
|-- postman-api-tests/
|-- rest-assured-api-tests/
|-- selenium-web-tests/
|-- build.gradle
|-- settings.gradle
`-- README.md
```

## Running Tests

Run commands from the repository root.

### Recommended review run

Runs the main portfolio validation: Playwright, REST Assured and Postman/Newman.

```bash
./gradlew portfolioTest
```

### Full project validation

Runs all module tests wired into each subproject build.

```bash
./gradlew clean build
```

### Full portfolio run

Runs the recommended portfolio validation plus Selenium, Appium and JMeter.

```bash
./gradlew portfolioFullTest
```

### API tests only

Runs REST Assured, Playwright API and Postman/Newman checks.

```bash
./gradlew apiTest
```

### Java/Gradle modules only

```bash
./gradlew javaTest
```

### Additional modules only

Runs the supporting Selenium, Appium and JMeter modules.

```bash
./gradlew additionalTest
```

### Run one module

Use the Gradle project path and task name:

```bash
./gradlew :rest-assured-api-tests:test
./gradlew :playwright-tests:playwrightApiTest
./gradlew :playwright-tests:playwrightTypeCheck
./gradlew :postman-api-tests:postmanTest
./gradlew :selenium-web-tests:test
./gradlew :appium-mobile-tests:test
./gradlew :jmeter-performance-tests:test
```

### Run REST Assured groups

```bash
./gradlew :rest-assured-api-tests:runSmokeTests
./gradlew :rest-assured-api-tests:runRegressionTests
```

## Playwright Local Setup

Playwright browser runs require browser binaries:

```bash
cd playwright-tests
npm install
npx playwright install
```

Useful Playwright module commands:

```bash
npm test
npm run test:api
npm run test:ui
npm run test:e2e
npm run typecheck
npm run lint
```

## Recommended Review Path

If you are reviewing this portfolio quickly, start here:

1. [playwright-tests](playwright-tests) - most complete module
2. [rest-assured-api-tests](rest-assured-api-tests) - Java API coverage
3. [postman-api-tests](postman-api-tests) - Postman/Newman API coverage

The remaining modules demonstrate additional tool coverage across Selenium,
Appium and JMeter.

## Notes

- The Playwright module is the most complete part of this portfolio.
- Appium tests require a local Appium/mobile browser setup.
- JMeter examples are included as supporting performance-testing examples.
- The repository is intended as a QA automation portfolio and technical review case study.

## Contact

- **Location:** Prague, Czech Republic
- **Email:** viktory.toth@gmail.com
- **LinkedIn:** [linkedin.com/in/tothviki](https://www.linkedin.com/in/tothviki/)
- **GitHub:** [github.com/tothviki](https://github.com/tothviki)