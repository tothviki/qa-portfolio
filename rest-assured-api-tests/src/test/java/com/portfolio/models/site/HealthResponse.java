package com.portfolio.models.site;

import java.util.List;

public record HealthResponse(
        String status,
        List<String> groups
) {
}
