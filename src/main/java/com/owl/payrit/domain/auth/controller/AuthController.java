package com.owl.payrit.domain.auth.controller;

import com.owl.payrit.domain.auth.domain.OauthProvider;
import com.owl.payrit.domain.auth.dto.request.LoginTokenRequest;
import com.owl.payrit.domain.auth.dto.response.LoginUser;
import com.owl.payrit.domain.auth.dto.response.TokenResponse;
import com.owl.payrit.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/oauth")
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/{oauthProvider}")
    public ResponseEntity<TokenResponse> login(@PathVariable OauthProvider oauthProvider, @RequestBody LoginTokenRequest loginTokenRequest) {
        log.debug("login request {}", oauthProvider);
        TokenResponse tokenResponse = authService.login(oauthProvider, loginTokenRequest.accessToken());
        return ResponseEntity.ok().body(tokenResponse);
    }

    @GetMapping("/test/{email}")
    public ResponseEntity<TokenResponse> testLogin(@PathVariable(name = "email") String email) {
        TokenResponse tokenResponse = authService.createTokenForTest(email);
        return ResponseEntity.ok().body(tokenResponse);
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal LoginUser loginUser) {
        log.debug("'{}' member request logout", loginUser.oauthInformation().getOauthProviderId());
        authService.logout(loginUser);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/leave")
    public ResponseEntity<Void> leave(@AuthenticationPrincipal LoginUser loginUser) {
        log.debug("'{}' member request leave paylit", loginUser.oauthInformation().getOauthProviderId());
        authService.leave(loginUser);
        return ResponseEntity.noContent().build();
    }
}
