package com.owl.payrit.domain.auth.dto.response;

public record TokenResponse(
    Long id,
    String accessToken,
    String refreshToken
) {

}
