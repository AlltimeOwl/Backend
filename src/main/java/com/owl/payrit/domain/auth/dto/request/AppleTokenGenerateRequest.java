package com.owl.payrit.domain.auth.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record AppleTokenGenerateRequest(
    String clientId,
    String clientSecret,
    String code,
    String grantType
) {
    public AppleTokenGenerateRequest(String clientId, String clientSecret, String code) {
        this (
            clientId,
            clientSecret,
            code,
            "authorization_code"
        );
    }
}
