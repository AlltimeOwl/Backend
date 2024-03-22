package com.owl.payrit.domain.auth.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

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

    public MultiValueMap<String, String> toRequestBody() {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("client_id", clientId);
        requestBody.add("client_secret", clientSecret);
        requestBody.add("code", code);
        requestBody.add("grant_type", "authorization_code");
        return requestBody;
    }
}
