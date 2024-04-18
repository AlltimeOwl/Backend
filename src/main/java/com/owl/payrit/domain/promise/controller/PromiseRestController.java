package com.owl.payrit.domain.promise.controller;


import com.owl.payrit.domain.auth.dto.response.LoginUser;
import com.owl.payrit.domain.promise.dto.request.PromiseWriteRequest;
import com.owl.payrit.domain.promise.dto.response.PromiseListResponse;
import com.owl.payrit.domain.promise.service.PromiseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/promise")
public class PromiseRestController {

    private final PromiseService promiseService;

    @PostMapping("/write")
    public ResponseEntity<Void> write(@AuthenticationPrincipal LoginUser loginUser,
                                      @RequestBody PromiseWriteRequest promiseWriteRequest) {

        log.info("{promise write rq : user_%d }".formatted(loginUser.id()));

        promiseService.write(loginUser, promiseWriteRequest);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/list")
    public ResponseEntity<List<PromiseListResponse>> list(@AuthenticationPrincipal LoginUser loginUser) {

        log.info("{promise list rq : user_%d}".formatted(loginUser.id()));

        List<PromiseListResponse> promiseListResponses = promiseService.getList(loginUser);

        return ResponseEntity.ok().body(promiseListResponses);
    }
}
