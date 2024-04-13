package com.owl.payrit.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Deprecated
public record RevokeRequest(
    @Schema(description = "탈퇴에 필요한 oauth code / 카카오는 임시 값(아무거나), 애플은 authorizationCode")
    String oauthCode
) {

}
