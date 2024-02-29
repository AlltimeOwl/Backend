package com.owl.payrit.domain.auth.controller;

import com.owl.payrit.domain.auth.domain.OauthProvider;
import com.owl.payrit.domain.auth.dto.request.LoginTokenRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/oauth")
@RestController
public class AuthController {

    @GetMapping("/{oauthProvider}")
    public ResponseEntity<String> login(@PathVariable OauthProvider oauthProvider, @RequestBody LoginTokenRequest loginTokenRequest) {

        return ResponseEntity.ok().build();
    }
}
