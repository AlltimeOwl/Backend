package com.owl.payrit.domain.auth.controller;

import com.owl.payrit.domain.auth.domain.OauthProvider;
import com.owl.payrit.domain.auth.dto.request.LoginTokenRequest;
import com.owl.payrit.domain.auth.dto.request.RevokeRequest;
import com.owl.payrit.domain.auth.dto.response.LoginUser;
import com.owl.payrit.domain.auth.dto.response.TokenRefreshResponse;
import com.owl.payrit.domain.auth.dto.response.TokenResponse;
import com.owl.payrit.domain.auth.service.AuthService;
import com.owl.payrit.domain.notification.service.NotificationHelper;
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
public class AuthController implements AuthApiDocs{

    private final AuthService authService;
    private final NotificationHelper notificationHelper;

    @Override
    @PostMapping("/{oauthProvider}")
    public ResponseEntity<TokenResponse> login(@PathVariable("oauthProvider") OauthProvider oauthProvider, @RequestBody LoginTokenRequest loginTokenRequest) {
        log.info("login request {}", oauthProvider);
        TokenResponse tokenResponse = authService.login(oauthProvider, loginTokenRequest);
        return ResponseEntity.ok().body(tokenResponse);
    }

    @Override
    @GetMapping("/test/{email}")
    public ResponseEntity<TokenResponse> testLogin(@PathVariable(name = "email") String email) {
        TokenResponse tokenResponse = authService.createTokenForTest(email);
        return ResponseEntity.ok().body(tokenResponse);
    }

    @Override
    @GetMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal LoginUser loginUser) {
        log.info("'{}' member request logout", loginUser.oauthInformation().getOauthProviderId());
        authService.logout(loginUser);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PostMapping("/revoke")
    public ResponseEntity<Void> revoke(@AuthenticationPrincipal LoginUser loginUser, @RequestBody RevokeRequest revokeRequest) {
        log.info("'{}' member requests revoke payrit", loginUser.oauthInformation().getOauthProviderId());
        authService.revoke(loginUser, revokeRequest);
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/check")
    public ResponseEntity<Void> checkAuthenticationStatus(@AuthenticationPrincipal LoginUser loginUser) {
        log.info("check '{}' user authenticationStatus", loginUser.oauthInformation().getOauthProviderId());
        boolean status = authService.checkAuthentication(loginUser);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PostMapping("/refresh")
    public ResponseEntity<TokenRefreshResponse> refreshAccessToken(@RequestBody LoginTokenRequest loginTokenRequest) {
        log.info("user request refresh AccessToken");
        TokenRefreshResponse tokenResponse = authService.refreshAccessToken(loginTokenRequest);
        return ResponseEntity.ok().body(tokenResponse);
    }

    @GetMapping("/hello")
    public ResponseEntity<Void> hello() {
        notificationHelper.hello();
        return ResponseEntity.noContent().build();
    }


}
