package com.owl.payrit.domain.auth.controller;

import com.owl.payrit.domain.auth.domain.OauthProvider;
import com.owl.payrit.domain.auth.dto.request.LoginTokenRequest;
import com.owl.payrit.domain.auth.dto.request.RevokeRequest;
import com.owl.payrit.domain.auth.dto.response.LoginUser;
import com.owl.payrit.domain.auth.dto.response.TokenRefreshResponse;
import com.owl.payrit.domain.auth.dto.response.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "회원 관리 API", description = "OAUTH2 기반의 회원 관리 API 입니다.")
public interface AuthApiDocs {

    @Operation(summary = "회원가입 & 로그인 처리하는 API", description = "Oauth에 맞는 Token으로 request하면, 로그인 & 회원가입 후 자체 JWT를 반환합니다.")
    @ApiResponse(responseCode = "200", description = "JWT를 반환합니다",
        content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = TokenResponse.class))
        })
    ResponseEntity<TokenResponse> login(
        @Parameter(description = "oauthProvider(kakao, apple)", required = true) OauthProvider oauthProvider,
        @RequestBody @Schema(implementation = LoginTokenRequest.class) LoginTokenRequest loginTokenRequest
    );

    ResponseEntity<TokenResponse> testLogin(@PathVariable(name = "email") String email);

    @Operation(summary = "로그아웃 API", description = "서버에서 유저에 대한 Token 정보를 삭제합니다. 이후에 디바이스에서도 저장된 Token을 삭제해야 합니다.")
    @ApiResponse(responseCode = "204")
    ResponseEntity<Void> logout(@AuthenticationPrincipal LoginUser loginUser);

    @Operation(summary = "회원탈퇴 API", description = "서버에서 유저에 대한 정보를 삭제합니다. 이후에 디바이스에서도 저장된 Token을 삭제해야 합니다.")
    @ApiResponse(responseCode = "204")
    ResponseEntity<Void> revoke(@AuthenticationPrincipal LoginUser loginUser, RevokeRequest revokeRequest);

    @Operation(summary = "본인 인증 여부 확인 API", description = "본인인증이 완료된 유저인지 확인합니다.")
    @ApiResponse(responseCode = "204")
    ResponseEntity<Void> checkAuthenticationStatus(@AuthenticationPrincipal LoginUser loginUser);

    @Operation(summary = "RefreshToken으로 AccessToken을 재발급합니다.")
    @ApiResponse(responseCode = "200",
        content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = TokenResponse.class))
        })
    ResponseEntity<TokenRefreshResponse> refreshAccessToken(LoginTokenRequest loginTokenRequest);
}
