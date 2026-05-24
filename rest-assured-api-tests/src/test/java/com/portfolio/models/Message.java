package com.portfolio.models;

public record Message(
        Integer id,
        String name,
        String subject,
        Boolean read
) {
}
