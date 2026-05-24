package com.portfolio.models;

import java.util.List;

public record Room(
        Integer roomid,
        String roomName,
        String type,
        Boolean accessible,
        String description,
        List<String> features,
        String image,
        Integer roomPrice
) {
}
