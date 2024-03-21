package com.owl.payrit.domain.auth.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record AppleTokenRevokeRequest(
    String clientId,
    String clientSecret,
    String token,
    String tokenTypeHint
) {
    public AppleTokenRevokeRequest(String clientId, String clientSecret, String token) {
        this (
            clientId,
            clientSecret,
            token,
            "refresh_token"
        );
    }
}
