package com.portfolio.models;

import java.util.List;

public record MessagesResponse(
        List<Message> messages
) {
}
