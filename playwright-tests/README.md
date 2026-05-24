# Playwright Tests

Playwright + TypeScript portfolio tests for the public Automation in Testing / Shady Meadows B&B demo site.

The suite targets `https://automationintesting.online` by default. Set `AUTOMATION_IN_TESTING_BASE_URL` to run against another deployment.

## Coverage Snapshot

- 36 test cases total
- 23 API tests, 12 UI tests, 1 E2E test
- 7 configured projects across API, UI, and E2E browser coverage
- HTML report: `test-reports/html`
- JSON results: `test-reports/results.json`

## Stack

- TypeScript
- Playwright Test
- Zod schema validation
- ESLint and Prettier
- npm for Node dependency management
- Gradle wrapper tasks for repository-level execution

## Coverage

| Layer | Location  | Playwright project                     | Scope                                                                                                                              |
| ----- | --------- | -------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------- |
| API   | `src/api` | `api`                                  | Auth, room discovery, booking create/read/update/delete, booking consistency, edge cases, health, branding, and message contracts. |
| UI    | `src/ui`  | `ui-chrome`, `ui-firefox`, `ui-webkit` | Public booking-page search, room details, reservation entry points, contact form behavior, and browser compatibility checks.       |
| E2E   | `src/e2e` | `e2e`, `e2e-firefox`, `e2e-webkit`     | Cross-layer checks where API-created booking data changes public room availability.                                                |

## Reviewer Path
For a fast review, inspect these areas first:
1. `src/fixtures/automationInTesting.fixture.ts` - shared API fixture and cleanup.
2. `src/utils/automationInTestingClient.ts` - reusable API client abstraction.
3. `src/pages/BookingPage.ts` and `src/pages/ContactPage.ts` - page-object structure.
4. `src/api/bookingCreate.spec.ts`, `src/api/bookingEdgeCases.spec.ts`, and `src/e2e/bookingFlow.spec.ts` - representative API and E2E coverage.
5. `playwright.config.ts` - project matrix, reporters, retry behavior, and CI settings.

## Project Layout

```text
playwright-tests/
|-- src/api/                         # API tests
|-- src/e2e/                         # API + UI cross-layer journey
|-- src/fixtures/                    # Custom Playwright fixtures and cleanup
|-- src/models/                      # TypeScript domain models
|-- src/pages/                       # Page objects
|-- src/schemas/                     # Zod API response schemas
|-- src/ui/                          # Browser UI tests
|-- src/utils/                       # API client, assertions, and test data
|-- playwright.config.ts             # Projects, reporters, browser settings
|-- package.json                     # npm scripts and dependencies
`-- build.gradle                     # Gradle task wiring
```

## Test Design

Every test has exactly one layer tag:

- `@api`
- `@ui`
- `@e2e`

Purpose tags are additive:

- `@smoke`: small high-signal subset for fast health checks.
- `@regression`: broader behavior or negative-path checks.
- `@contract`: schema or response contract checks.
- `@cross-browser`: UI checks intended for Chromium, Firefox, and WebKit projects.

Generated data is run-scoped with `TEST_RUN_ID` or a timestamp fallback from `AutomationInTestingTestData`. API-created bookings are registered with `automationApi.trackBooking()` and deleted during fixture teardown.

UI booking tests are intentionally read-only for persistent room bookings. They validate availability search, reservation entry points, room details, contact forms, and pre-submit cancellation without completing a public-site booking. Full booking create/update/delete assertions are API-level, with the current E2E journey using API-managed setup to verify that a booked room becomes unavailable in the UI.

## Design Choices
- API-created bookings are tracked and deleted during fixture teardown to avoid polluting the public demo service.
- Browser tests avoid final public booking submission and focus on observable UI behavior that can be validated safely.
- API tests own booking CRUD depth because they are faster, more stable, and easier to clean up than equivalent UI flows.
- Cross-browser coverage is focused on high-signal UI behaviors instead of duplicating every scenario across every browser.

## Setup

From the module directory:

```bash
cd playwright-tests
npm install
npx playwright install
```

## Run With npm

Run commands from `playwright-tests/`.

```bash
npm test                       # all configured Playwright projects
npm run test:api               # API project only
npm run test:web               # Chromium UI project
npm run test:smoke             # tests tagged @smoke
npm run test:e2e               # Chromium E2E project
npm run test:e2e:all           # Chromium, Firefox, and WebKit E2E projects
npm run test:ui:chrome         # Chromium UI project
npm run test:ui:firefox        # Firefox UI project
npm run test:ui:webkit         # WebKit UI project
npm run test:ui:cross-browser  # all UI browser projects
npm run test:debug             # Playwright debug mode
npm run test:ui                # Playwright UI mode
npm run lint
npm run format:check
npm run typecheck
npm run report
```

Run a specific file or tagged subset:

```bash
npx playwright test src/e2e/bookingFlow.spec.ts --project=e2e
npx playwright test --grep @cross-browser
npx playwright test --project=ui-firefox src/ui/crossBrowser.spec.ts
```

## Run With Gradle

Run commands from the repository root.

```bash
./gradlew :playwright-tests:playwrightTest       # active Playwright suite: npm test
./gradlew :playwright-tests:playwrightApiTest    # API project only
./gradlew :playwright-tests:playwrightUiTest     # Chromium UI project
./gradlew :playwright-tests:playwrightWebTest    # Chromium UI project
./gradlew :playwright-tests:playwrightSmokeTest  # tests tagged @smoke
./gradlew :playwright-tests:playwrightTypeCheck  # TypeScript type checking
./gradlew :playwright-tests:playwrightLint       # ESLint
./gradlew :playwright-tests:playwrightReport     # open the HTML report
```

Repository-level tasks:

```bash
./gradlew apiTest             # REST Assured + Playwright API + Postman
./gradlew portfolioTest       # recommended portfolio validation
./gradlew portfolioFullTest   # recommended validation plus additional modules
./gradlew clean build         # standard multi-project Gradle lifecycle sanity check
```

The active Playwright suite includes browser projects. Install Playwright browser binaries before running it locally or in CI.

## Browser Coverage

The config defines separate projects for the same UI and E2E specs:

- `ui-chrome`: Desktop Chrome settings
- `ui-firefox`: Desktop Firefox settings
- `ui-webkit`: Desktop Safari/WebKit settings
- `e2e`: Desktop Chrome settings
- `e2e-firefox`: Desktop Firefox settings
- `e2e-webkit`: Desktop Safari/WebKit settings

`src/ui/crossBrowser.spec.ts` contains focused compatibility checks for page load, availability controls, room cards, and reservation navigation. The broader UI specs also run under all three UI projects when using `npm run test:ui:cross-browser`.

## Current Test Files

API specs:

- `src/api/authApi.spec.ts`
- `src/api/bookingConsistency.spec.ts`
- `src/api/bookingCreate.spec.ts`
- `src/api/bookingDelete.spec.ts`
- `src/api/bookingEdgeCases.spec.ts`
- `src/api/bookingRead.spec.ts`
- `src/api/bookingUpdate.spec.ts`
- `src/api/healthApi.spec.ts`
- `src/api/roomsApi.spec.ts`
- `src/api/siteContentApi.spec.ts`

UI specs:

- `src/ui/bookingAvailability.spec.ts`
- `src/ui/bookingCancel.spec.ts`
- `src/ui/bookingCreate.spec.ts`
- `src/ui/contactFormValidation.spec.ts`
- `src/ui/crossBrowser.spec.ts`
- `src/ui/messageSend.spec.ts`
- `src/ui/roomsDetails.spec.ts`
- `src/ui/roomsFilter.spec.ts`
- `src/ui/searchNoResults.spec.ts`

E2E specs:

- `src/e2e/bookingFlow.spec.ts`

## Implementation Notes

- `src/fixtures/automationInTesting.fixture.ts` provides the `automationApi` client fixture and deletes tracked bookings after each test.
- `src/utils/automationInTestingClient.ts` centralizes API calls.
- `src/utils/automationInTestingTestData.ts` generates isolated booking and message data.
- `src/pages/BookingPage.ts` and `src/pages/ContactPage.ts` encapsulate browser interactions.
- Prefer page objects and shared API helpers when adding tests.
- Track every API-created booking that should be cleaned up with `automationApi.trackBooking()`.
- Use resilient user-facing selectors and explicit expectations around page transitions.

## Reports And Artifacts

- HTML report: `test-reports/html`
- JSON results: `test-reports/results.json`
- Failure traces, screenshots, and videos: `test-results/<project>`

The Playwright config is CI-aware: CI runs forbid `test.only`, use two workers, retry failures twice, and retain failure traces, screenshots, and videos. Local runs keep retries disabled and use the default Playwright worker count.

## Skipping Browser Tests In CI

If a CI job should avoid browser installation or browser runtime, run API-only tasks or exclude Playwright browser tasks.

```bash
./gradlew apiTest
./gradlew :playwright-tests:playwrightApiTest
./gradlew clean build -x :playwright-tests:playwrightTest
./gradlew clean build -x :playwright-tests:playwrightUiTest
```

`-x` skips the named task for that Gradle invocation only.
