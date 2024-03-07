package com.owl.payrit.domain.promissorypaper.controller;

import com.owl.payrit.domain.auth.dto.response.LoginUser;
import com.owl.payrit.domain.promissorypaper.dto.request.PaperModifyRequest;
import com.owl.payrit.domain.promissorypaper.dto.request.PaperWriteRequest;
import com.owl.payrit.domain.promissorypaper.dto.response.PaperDetailResponse;
import com.owl.payrit.domain.promissorypaper.dto.response.PaperListResponse;
import com.owl.payrit.domain.promissorypaper.entity.PaperRole;
import com.owl.payrit.domain.promissorypaper.service.PromissoryPaperService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
                                                      @PathVariable(value = "id") Long id) {

        log.info("request paper id : " + id);

        PaperDetailResponse paperDetailResponse = promissoryPaperService.getDetail(loginUser, id);

        return ResponseEntity.ok().body(paperDetailResponse);
    }

    @GetMapping("/list")
    public ResponseEntity<List<PaperListResponse>> creditorList(@AuthenticationPrincipal LoginUser loginUser) {

        log.info("request user id : " + loginUser.id());

        List<PaperListResponse> allListResponses = promissoryPaperService.getAllListResponse(loginUser);

        return ResponseEntity.ok().body(allListResponses);
    }

    @PutMapping("/approve/accept/{id}")
    public ResponseEntity<String> acceptPaper(@AuthenticationPrincipal LoginUser loginUser,
                                              @PathVariable(value = "id") Long paperId) {

        log.info("request user id : " + loginUser.id());

        promissoryPaperService.acceptPaper(loginUser, paperId);

        return ResponseEntity.ok().body("accept");
    }

    @PostMapping("/modify/request/{id}")
    public ResponseEntity<String> requestModify(@AuthenticationPrincipal LoginUser loginUser,
                                                @PathVariable(value = "id") Long paperId,
                                                @RequestBody PaperModifyRequest paperModifyRequest) {

        log.info("request user id : " + loginUser.id());
        log.info("contents : " + paperModifyRequest.contents());

        promissoryPaperService.sendModifyRequest(loginUser, paperId, paperModifyRequest);

        return ResponseEntity.ok().body("modify request : %s".formatted(paperModifyRequest.contents()));
    }
}
