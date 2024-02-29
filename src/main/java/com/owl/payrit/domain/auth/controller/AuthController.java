package com.owl.payrit.domain.auth.controller;

import com.owl.payrit.domain.auth.domain.OauthProvider;
import com.owl.payrit.domain.auth.dto.request.LoginTokenRequest;
import com.owl.payrit.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> login(@PathVariable OauthProvider oauthProvider, @RequestBody LoginTokenRequest loginTokenRequest) {
        log.debug("login request {}", oauthProvider);
        authService.login(oauthProvider, loginTokenRequest.accessToken());
        return ResponseEntity.ok().build();
    }
}
