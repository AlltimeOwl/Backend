package com.owl.payrit.domain.promissorypaper.controller;

import com.owl.payrit.domain.auth.dto.response.LoginUser;
import com.owl.payrit.domain.promissorypaper.dto.request.PaperWriteRequest;
import com.owl.payrit.domain.promissorypaper.dto.response.PaperDetailResponse;
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
    public ResponseEntity<PaperDetailResponse> detail(@AuthenticationPrincipal LoginUser loginUser,
                                                      @PathVariable(value="id") Long id) {

        log.info("request paper id : " + id);

        PaperDetailResponse paperDetailResponse = promissoryPaperService.getDetail(loginUser, id);

        return ResponseEntity.ok().body(paperDetailResponse);
    }

    @GetMapping("/list")
    public ResponseEntity<String> creditorList(@AuthenticationPrincipal LoginUser loginUser) {

        log.info("request user id : " + loginUser.id());

        return ResponseEntity.ok().body("list");
    }
}
