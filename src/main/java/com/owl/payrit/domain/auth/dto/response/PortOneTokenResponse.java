package com.owl.payrit.domain.auth.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record PortOneTokenResponse(
    int code,
    String message,
    AuthAnnotation authAnnotation
) {
    @JsonNaming(SnakeCaseStrategy.class)
    public record AuthAnnotation(
        String accessToken,
        int expiredAt,
        int now
    ) {

    }
}


