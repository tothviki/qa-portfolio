package com.portfolio.performance.core;

import lombok.Builder;

@Builder
public record ScenarioResult(long sampleCount,
                             long failureCount,
                             long averageResponseTimeMillis,
                             long maxResponseTimeMillis) {

    public String describe() {
        return "samples=" + sampleCount
                + ", failures=" + failureCount
                + ", avgMs=" + averageResponseTimeMillis
                + ", maxMs=" + maxResponseTimeMillis;
    }
}
