# QA Automation Portfolio – Viktória Tóth

[![CI](https://github.com/tothviki/qa-portfolio/actions/workflows/ci.yml/badge.svg?branch=main)](https://github.com/tothviki/qa-portfolio/actions/workflows/ci.yml)
![Automated Tests](https://img.shields.io/badge/Automated%20Tests-Passing-brightgreen?style=flat&logo=checkmarx&logoColor=white)
![Playwright](https://img.shields.io/badge/Framework-Playwright-2EAD33?style=flat&logo=playwright&logoColor=white)

Senior QA Automation / SDET portfolio demonstrating API, web, mobile and
performance testing skills with TypeScript, Java, Playwright, REST Assured,
Postman/Newman, Selenium WebDriver, Appium and JMeter.

This repository contains focused and documented testing projects that show
practical QA automation skills, maintainable test structure, regression
thinking and multi-tool testing experience. It is organized as a case study
with one flagship module and supporting layers beneath it.

## Portfolio Snapshot

| Tier | Module | Coverage | Report |
| --- | --- | --- | --- |
| Flagship | [playwright-tests](playwright-tests) | 36 test cases: 23 API, 12 UI, 1 E2E across 7 projects | `playwright-tests/test-reports/html` |
| Core API | [postman-api-tests](postman-api-tests) | 24 checks across focused auth, booking, public API, and edge-case collections | `postman-api-tests/build/reports/postman/*-newman-report.json` |
| Core API | [rest-assured-api-tests](rest-assured-api-tests) | 22 TestNG tests across auth, booking CRUD, and public API checks | `rest-assured-api-tests/build/reports/tests/test/index.html` |
| Supporting | [selenium-web-tests](selenium-web-tests), [appium-mobile-tests](appium-mobile-tests), [jmeter-performance-tests](jmeter-performance-tests) | Additional browser, mobile, and performance coverage | Module-specific outputs |

## Portfolio Highlights

- Flagship case study: Playwright + TypeScript API, UI and E2E automation suite
- Core API case studies: Postman/Newman and REST Assured
- Supporting modules: Selenium WebDriver, Appium and JMeter
- Repository-level Gradle tasks for repeatable portfolio validation
- Module-level commands for focused API, UI, mobile and performance test runs

## Tech Stack

- **Programming:** TypeScript, Java, Python
- **Test Automation:** Playwright, REST Assured, Postman/Newman, Selenium WebDriver, Appium
- **Performance Testing:** JMeter
- **Build & Execution:** Gradle, npm
- **Quality & Tooling:** GitHub Actions, TestNG, PyTest, ESLint, Prettier
- **Testing Areas:** API, Web UI, Mobile, E2E, Performance, Regression

## Projects

### Flagship Case Study: Playwright API, UI and E2E Automation

**Playwright + TypeScript**

Modern test automation suite for the public Automation in Testing / Shady
Meadows B&B demo application. Covers API, UI and E2E scenarios across booking,
room discovery, validation, health, contact form and cross-browser workflows.

Demonstrates maintainable Playwright test architecture with API helpers, page
objects, reusable fixtures, structured test data, cleanup logic, schema
validation and reporting.

→ [playwright-tests](playwright-tests)

### Core API Case Studies

#### Postman API Tests

**Postman + Newman**

Focused Postman collections with Newman command-line execution for API checks
that are easy to review, run locally and integrate into automated workflows.

→ [postman-api-tests](postman-api-tests)

#### REST Assured API Tests

**REST Assured + Java**

Java-based API test suite demonstrating request and response validation,
reusable test structure and regression-oriented API checks.

→ [rest-assured-api-tests](rest-assured-api-tests)

### Supporting Modules

#### Selenium Web Tests

**Selenium WebDriver + Java**

Web UI automation examples demonstrating browser-based testing structure and
Selenium WebDriver usage.

→ [selenium-web-tests](selenium-web-tests)

#### Appium Mobile Tests

**Appium + Java**

Mobile automation examples demonstrating Appium-based test structure for mobile
QA scenarios.

→ [appium-mobile-tests](appium-mobile-tests)

#### JMeter Performance Tests

**JMeter + Gradle**

Performance testing examples integrated with Gradle for repeatable execution.

→ [jmeter-performance-tests](jmeter-performance-tests)

## Repository Structure
```
    qa-portfolio/
    ├── appium-mobile-tests/
    ├── jmeter-performance-tests/
    ├── playwright-tests/
    ├── postman-api-tests/
    ├── rest-assured-api-tests/
    ├── selenium-web-tests/
    ├── build.gradle
    ├── settings.gradle
    └── README.md
```
## Running Tests

Run commands from the repository root.

### Run the default portfolio validation

Runs the reliable portfolio checks, including Java modules, REST Assured API
tests, Playwright active suite checks and Postman/Newman API tests.

```bash
./gradlew portfolioTest
```

The standard Gradle lifecycle is wired to the same portfolio validation:

```bash
./gradlew clean build
```

Note: the Playwright active suite may include UI/browser tests, which require
Playwright browser binaries and can increase build runtime. For faster API-only
or focused runs, use the module-level tasks below.

### Run all wired suites

```bash
./gradlew portfolioFullTest
```

### Run API tests only

Runs REST Assured, Playwright API and Postman/Newman checks:

```bash
./gradlew apiTest
```

### Run Java/Gradle modules only

```bash
./gradlew javaTest
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

### Run a subset of modules

Pass multiple tasks in one command:

```bash
./gradlew :rest-assured-api-tests:test :postman-api-tests:postmanTest
./gradlew :playwright-tests:playwrightApiTest :postman-api-tests:postmanTest
./gradlew :selenium-web-tests:test :appium-mobile-tests:test
```

### Run REST Assured groups

```bash
./gradlew :rest-assured-api-tests:runSmokeTests
./gradlew :rest-assured-api-tests:runRegressionTests
```

### Clean generated outputs

```bash
./gradlew clean
```

## What This Portfolio Demonstrates

- Current hands-on QA automation practice with modern Playwright + TypeScript
- API testing across multiple approaches: Playwright API, REST Assured and Postman/Newman
- Maintainable test design with fixtures, helpers, page objects and reusable test data
- Regression-oriented test coverage across API, web, mobile and performance areas
- Practical experience with Gradle-based orchestration across multiple QA modules
- Ability to document, structure and maintain test automation projects for review

## Recommended Review Path

If you are reviewing this portfolio quickly, start here:

1. [playwright-tests](playwright-tests) – strongest and most complete module
2. [postman-api-tests](postman-api-tests) – secondary API case study
3. [rest-assured-api-tests](rest-assured-api-tests) – secondary API case study

The remaining modules demonstrate additional tool coverage across Selenium,
Appium and JMeter.

## Contact

- **Location:** Prague, Czech Republic
- **Email:** viktory.toth@gmail.com
- **LinkedIn:** [linkedin.com/in/tothviki](https://www.linkedin.com/in/tothviki/)
