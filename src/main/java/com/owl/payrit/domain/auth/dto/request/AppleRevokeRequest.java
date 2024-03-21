package com.owl.payrit.domain.auth.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

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

    public MultiValueMap<String, String> toRequestBody() {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("client_id", clientId);
        requestBody.add("client_secret", clientSecret);
        requestBody.add("token", token);
        requestBody.add("token_type_hint", tokenTypeHint);
        return requestBody;
    }
}
