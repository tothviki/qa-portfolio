package com.portfolio.models.site;

public record BrandingResponse(
        String name,
        MapCoordinates map,
        ContactDetails contact
) {
    public record MapCoordinates(
            Double latitude,
            Double longitude
    ) {
    }

    public record ContactDetails(
            String name,
            String address,
            String phone,
            String email
    ) {
    }
}
