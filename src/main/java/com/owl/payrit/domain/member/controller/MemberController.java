package com.owl.payrit.domain.member.controller;

import com.owl.payrit.domain.auth.dto.response.LoginUser;
import com.owl.payrit.domain.member.dto.SignUpRequest;
import com.owl.payrit.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/account")
@RestController
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/kakao/signup")
    public ResponseEntity<String> hello(@RequestBody SignUpRequest signUpRequest, @AuthenticationPrincipal LoginUser loginUser){
        log.debug(" ");
        return ResponseEntity.ok().body("hello");
    }

    @GetMapping("/")
    public ResponseEntity<LoginUser> test(@AuthenticationPrincipal LoginUser loginUser) {
        return ResponseEntity.ok().body(loginUser);
    }
}
