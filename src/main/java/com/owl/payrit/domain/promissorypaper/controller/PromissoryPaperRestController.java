package com.owl.payrit.domain.promissorypaper.controller;

import com.owl.payrit.domain.auth.dto.response.LoginUser;
import com.owl.payrit.domain.promissorypaper.dto.request.PaperWriteRequest;
import com.owl.payrit.domain.promissorypaper.dto.response.CreditorPaperResponse;
import com.owl.payrit.domain.promissorypaper.dto.response.DebtorPaperResponse;
import com.owl.payrit.domain.promissorypaper.dto.response.PaperDetailResponse;
import com.owl.payrit.domain.promissorypaper.service.PromissoryPaperService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
    public ResponseEntity<PaperDetailResponse> detail(@AuthenticationPrincipal LoginUser loginUser,
                                                      @PathVariable(value="id") Long id) {

        log.info("request paper id : " + id);

        PaperDetailResponse paperDetailResponse = null;       //TODO: 내용 대입 필요

        return ResponseEntity.ok().body(paperDetailResponse);
    }

    @GetMapping("/list/creditor")
    public ResponseEntity<List<CreditorPaperResponse>> creditorList(@AuthenticationPrincipal LoginUser loginUser) {

        log.info("request user id : " + loginUser.id());

        List<CreditorPaperResponse> creditorPaperResponseList = new ArrayList<>();  //TODO: 내용 대입 필요

        return ResponseEntity.ok().body(creditorPaperResponseList);
    }

    @GetMapping("/list/debtor")
    public ResponseEntity<List<DebtorPaperResponse>> debtorList(@AuthenticationPrincipal LoginUser loginUser) {

        log.info("request user id : " + loginUser.id());
        
        List<DebtorPaperResponse> debtorPaperResponseList = new ArrayList<>(); //TODO: 내용 대입 필요

        return ResponseEntity.ok().body(debtorPaperResponseList);
    }
}
