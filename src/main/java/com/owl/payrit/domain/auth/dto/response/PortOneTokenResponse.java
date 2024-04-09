package com.owl.payrit.domain.auth.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

public record PortOneTokenResponse(
    Integer code,
    String message,
    AuthAnnotation response
) {
    @JsonNaming(SnakeCaseStrategy.class)
    public record AuthAnnotation(
        String accessToken,
        Integer now,
        Integer expiredAt
    ) {

    }
}


