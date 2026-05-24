package com.portfolio.performance.core;

import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleResult;

import java.util.concurrent.atomic.AtomicLong;

public class ResponseTracker extends ResultCollector {
    private final AtomicLong sampleCount = new AtomicLong();
    private final AtomicLong failureCount = new AtomicLong();
    private final AtomicLong totalResponseTimeMillis = new AtomicLong();
    private final AtomicLong maxResponseTimeMillis = new AtomicLong();

    @Override
    public void sampleOccurred(SampleEvent event) {
        SampleResult result = event.getResult();
        sampleCount.incrementAndGet();
        totalResponseTimeMillis.addAndGet(result.getTime());
        maxResponseTimeMillis.accumulateAndGet(result.getTime(), Math::max);

        if (!result.isSuccessful()) {
            failureCount.incrementAndGet();
        }

        super.sampleOccurred(event);
    }

    public ScenarioResult snapshot() {
        return ScenarioResult.builder()
                .sampleCount(sampleCount.get())
                .failureCount(failureCount.get())
                .averageResponseTimeMillis(sampleCount.get() == 0
                        ? 0
                        : totalResponseTimeMillis.get() / sampleCount.get())
                .maxResponseTimeMillis(maxResponseTimeMillis.get())
                .build();
    }
}
