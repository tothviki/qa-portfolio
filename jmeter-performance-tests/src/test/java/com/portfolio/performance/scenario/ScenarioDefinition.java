package com.portfolio.performance.scenario;

import lombok.Builder;

@Builder
public record ScenarioDefinition(
    String name,
    String method,
    String path,
    String body,
    int expectedStatus,
    int users,
    int loops,
    int rampUpSeconds) {
}
