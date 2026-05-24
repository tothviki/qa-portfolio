package com.portfolio.performance.load;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.portfolio.performance.core.JMeterScenarioRunner;
import com.portfolio.performance.core.ScenarioResult;
import com.portfolio.performance.scenario.ScenarioDefinition;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class AutomationInTestingLoadTest {
    private static final JMeterScenarioRunner RUNNER = JMeterScenarioRunner.createDefault();

    @BeforeAll
    static void configureJMeter() {
        RUNNER.configure();
    }

    @Test
    @Timeout(120)
    @DisplayName("POST /api/auth/login sustains lightweight submission load")
    void loginSubmissionLoadTest() {
        ScenarioResult result = RUNNER.run(ScenarioDefinition.builder()
                .name("automation-in-testing-login-load")
                .method("POST")
                .path("/api/auth/login")
                .body(authPayload())
                .expectedStatus(200)
                .users(3)
                .loops(2)
                .rampUpSeconds(2)
                .build());

        assertEquals(0, result.failureCount(), result.describe());
        assertEquals(6, result.sampleCount(), result.describe());
    }

    @Test
    @Timeout(90)
    @DisplayName("GET /api/room sustains concurrent catalog lookups")
    void roomCatalogLookupLoadTest() {
        ScenarioResult result = RUNNER.run(ScenarioDefinition.builder()
                .name("automation-in-testing-room-catalog-load")
                .method("GET")
                .path("/api/room")
                .expectedStatus(200)
                .users(4)
                .loops(2)
                .rampUpSeconds(1)
                .build());

        assertEquals(0, result.failureCount(), result.describe());
        assertEquals(8, result.sampleCount(), result.describe());
    }

    private static String authPayload() {
        return """
                {
                  "username": "admin",
                  "password": "password"
                }
                """;
    }
}
