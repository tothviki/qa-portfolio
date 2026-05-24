package com.portfolio.models.common;

public record MethodNotAllowedResponse(
        String timestamp,
        Integer status,
        String error,
        String path
) {
}
