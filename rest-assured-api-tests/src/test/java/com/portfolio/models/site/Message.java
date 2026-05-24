package com.portfolio.models.site;

public record Message(
        Integer id,
        String name,
        String subject,
        Boolean read
) {
}
