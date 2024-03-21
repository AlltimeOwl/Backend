package com.owl.payrit.domain.auth.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record AppleRevokeRequest(
    String clientId,
    String clientSecret,
    String token,
    String tokenTypeHint
) {
    public AppleRevokeRequest(String clientId, String clientSecret, String token) {
        this (
            clientId,
            clientSecret,
            token,
            "access_token"
        );
    }
}