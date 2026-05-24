package com.portfolio.models.common;

import java.util.List;

public record ValidationErrorsResponse(
        List<String> errors
) {
}
