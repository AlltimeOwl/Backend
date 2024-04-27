package com.owl.payrit.domain.promise.controller;


import com.owl.payrit.domain.auth.dto.response.LoginUser;
import com.owl.payrit.domain.promise.dto.request.PromiseWriteRequest;
import com.owl.payrit.domain.promise.dto.response.PromiseDetailResponse;
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
public class PromiseRestController implements PromiseApiDocs {

    private final PromiseService promiseService;

    @Override
    @PostMapping("/write")
    public ResponseEntity<Long> write(@AuthenticationPrincipal LoginUser loginUser,
                                      @RequestBody PromiseWriteRequest promiseWriteRequest) {

        log.info("{ promise write rq : user_%d }".formatted(loginUser.id()));

        Long promiseId = promiseService.write(loginUser, promiseWriteRequest);

        return ResponseEntity.ok(promiseId);
    }

    @Override
    @GetMapping("/list")
    public ResponseEntity<List<PromiseListResponse>> list(@AuthenticationPrincipal LoginUser loginUser) {

        log.info("{ promise list rq : user_%d }".formatted(loginUser.id()));

        List<PromiseListResponse> promiseListResponses = promiseService.getList(loginUser);

        return ResponseEntity.ok().body(promiseListResponses);
    }

    @Override
    @GetMapping("/detail/{id}")
    public ResponseEntity<PromiseDetailResponse> detail(@AuthenticationPrincipal LoginUser loginUser,
                                                        @PathVariable(name = "id") Long promiseId) {

        log.info("{ promise detail rq : user_%d }".formatted(loginUser.id()));

        PromiseDetailResponse detailResponse = promiseService.getDetail(loginUser, promiseId);

        return ResponseEntity.ok().body(detailResponse);
    }

    @Override
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> remove(@AuthenticationPrincipal LoginUser loginUser,
                                       @PathVariable(name = "id") Long promiseId) {

        log.info("{ promise remove rq : user_%d }".formatted(loginUser.id()));

        promiseService.remove(loginUser, promiseId);

        return ResponseEntity.noContent().build();
    }

    @Override
    @PostMapping("/share/{id}")
    public ResponseEntity<Void> share(@AuthenticationPrincipal LoginUser loginUser,
                                     @PathVariable(name = "id") Long promiseId) {

        log.info("{ promise share rq : user_%d }".formatted(loginUser.id()));

        promiseService.share(loginUser, promiseId);

        return ResponseEntity.noContent().build();
    }
}
