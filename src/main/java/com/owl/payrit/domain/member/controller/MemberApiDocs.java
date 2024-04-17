package com.owl.payrit.domain.member.controller;

import com.owl.payrit.domain.auth.dto.response.LoginUser;
import com.owl.payrit.domain.member.dto.response.CertificationResponse;
import com.owl.payrit.domain.member.dto.response.StatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Tag(name = "마이 페이지 API", description = "마이 페이지 API 입니다.")
public interface MemberApiDocs {

    @Operation(summary = "본인인증 정보를 불러옵니다.")
    @ApiResponse(responseCode = "200",
        content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = CertificationResponse.class))
        })
    ResponseEntity<CertificationResponse> getCertificationInformation(@AuthenticationPrincipal LoginUser loginUser);

    @Operation(summary = "알럼 수신 변경 API", description = "알람 수신 여부를 변경합니다. True -> false, false -> true")
    @ApiResponse(responseCode = "204")
    ResponseEntity<Void> modifyNotificationStatus(@AuthenticationPrincipal LoginUser loginUser);

    @Operation(summary = "알럼 수신 상태 조회 API", description = "알림 수신 상태를 조회합니다.")
    @ApiResponse(responseCode = "200")
    ResponseEntity<StatusResponse> getStatus(@AuthenticationPrincipal LoginUser loginUser);
}
