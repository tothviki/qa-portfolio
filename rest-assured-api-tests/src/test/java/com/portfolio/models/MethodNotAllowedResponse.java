package com.portfolio.models;

public record MethodNotAllowedResponse(
        String timestamp,
        Integer status,
        String error,
        String path
) {
}
