package com.portfolio.models;

import java.util.List;

public record ValidationErrorsResponse(
        List<String> errors
) {
}
