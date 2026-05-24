package com.portfolio.models.site;

import java.util.List;

public record MessagesResponse(
        List<Message> messages
) {
}
