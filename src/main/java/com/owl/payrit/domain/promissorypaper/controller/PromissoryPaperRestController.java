package com.owl.payrit.domain.promissorypaper.controller;

import com.owl.payrit.domain.auth.dto.response.LoginUser;
import com.owl.payrit.domain.promissorypaper.dto.request.PaperWriteRequest;
import com.owl.payrit.domain.promissorypaper.service.PromissoryPaperService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/paper")
public class PromissoryPaperRestController {

    private final PromissoryPaperService promissoryPaperService;

    @PostMapping("/write")
    public ResponseEntity<String> write(@AuthenticationPrincipal LoginUser loginUser,
                                             @RequestBody PaperWriteRequest paperWriteRequest) {

        log.info(paperWriteRequest.toString());

        promissoryPaperService.writePaper(loginUser, paperWriteRequest);

        return ResponseEntity.ok().body("write");
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> detail(@AuthenticationPrincipal LoginUser loginUser,
                                         @PathVariable(value="id") Long id) {

        log.info("request paper id : " + id);

        return ResponseEntity.ok().body("detail");
    }

    @GetMapping("/list/creditor")
    public ResponseEntity<String> creditorList(@AuthenticationPrincipal LoginUser loginUser) {

        log.info("request user id : " + loginUser.id());

        return ResponseEntity.ok().body("creditorList");
    }

    @GetMapping("/list/debtor")
    public ResponseEntity<String> debtorList(@AuthenticationPrincipal LoginUser loginUser) {

        log.info("request user id : " + loginUser.id());

        return ResponseEntity.ok().body("debtorList");
    }
}
