package com.portfolio.models;

import java.util.List;

public record HealthResponse(
        String status,
        List<String> groups
) {
}
