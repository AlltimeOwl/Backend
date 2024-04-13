package com.owl.payrit.domain.auth.dto.request;

public record LoginTokenRequest(
    String accessToken,
    String refreshToken,
    String firebaseToken,
    String authorizationCode
) {

}
