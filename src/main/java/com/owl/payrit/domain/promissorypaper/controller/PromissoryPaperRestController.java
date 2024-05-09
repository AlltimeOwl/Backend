package com.owl.payrit.domain.promissorypaper.controller;

import com.owl.payrit.domain.auth.dto.response.LoginUser;
import com.owl.payrit.domain.promissorypaper.dto.request.PaperModifyRequest;
import com.owl.payrit.domain.promissorypaper.dto.request.PaperWriteRequest;
import com.owl.payrit.domain.promissorypaper.dto.response.PaperDetailResponse;
import com.owl.payrit.domain.promissorypaper.dto.response.PaperListResponse;
import com.owl.payrit.domain.promissorypaper.service.PromissoryPaperService;
import com.owl.payrit.domain.repaymenthistory.dto.request.RepaymentCancelRequest;
import com.owl.payrit.domain.repaymenthistory.dto.request.RepaymentRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/paper")
public class PromissoryPaperRestController implements PromissoryPaperApiDocs {

    private final PromissoryPaperService promissoryPaperService;

    @Override
    @PostMapping("/write")
    public ResponseEntity<Void> write(@AuthenticationPrincipal LoginUser loginUser,
                                      @RequestBody PaperWriteRequest paperWriteRequest,
                                      HttpServletRequest req) throws IOException {

        log.info("{ paper write rq : user_%d }".formatted(loginUser.id()));

        promissoryPaperService.writePaper(loginUser, paperWriteRequest, req);

        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<PaperDetailResponse> detail(@AuthenticationPrincipal LoginUser loginUser,
                                                      @PathVariable(value = "id") Long paperId) {

        log.info("{ paper detail rq : user_%d, paper_%d }".formatted(loginUser.id(), paperId));

        PaperDetailResponse paperDetailResponse = promissoryPaperService.getDetail(loginUser, paperId);

        return ResponseEntity.ok().body(paperDetailResponse);
    }

    @Override
    @GetMapping("/list")
    public ResponseEntity<List<PaperListResponse>> list(@AuthenticationPrincipal LoginUser loginUser) {

        log.info("{ paper list rq : user_%d }".formatted(loginUser.id()));

        List<PaperListResponse> allListResponses = promissoryPaperService.getAllListResponse(loginUser);

        return ResponseEntity.ok().body(allListResponses);
    }

    @Override
    @PutMapping("/approve/accept/{id}")
    public ResponseEntity<Void> acceptPaper(@AuthenticationPrincipal LoginUser loginUser,
                                            @PathVariable(value = "id") Long paperId,
                                            @RequestPart("file") MultipartFile file,
                                            HttpServletRequest req) throws IOException {

        log.info("{ paper accept rq : user_%d, paper_%d }".formatted(loginUser.id(), paperId));

        promissoryPaperService.acceptPaper(loginUser, paperId, file, req);

        return ResponseEntity.noContent().build();
    }

    @Override
    @PostMapping("/modify/request")
    public ResponseEntity<Void> requestModify(@AuthenticationPrincipal LoginUser loginUser,
                                              @RequestBody PaperModifyRequest paperModifyRequest) {

        log.info("{ paper modify rq : user_%d, paper_%d }".formatted(loginUser.id(), paperModifyRequest.paperId()));
        log.info("{ modify rq contents : %s }".formatted(paperModifyRequest.contents()));

        promissoryPaperService.sendModifyRequest(loginUser, paperModifyRequest);

        return ResponseEntity.noContent().build();
    }

    @Override
    @PutMapping("/modify/accept/{id}")
    public ResponseEntity<Void> modifying(@AuthenticationPrincipal LoginUser loginUser,
                                          @PathVariable(value = "id") Long paperId,
                                          @RequestBody PaperWriteRequest paperWriteRequest) {

        log.info("{ paper modify accept rq : user_%d, paper_%d }".formatted(loginUser.id(), paperId));

        promissoryPaperService.modifyingPaper(loginUser, paperId, paperWriteRequest);

        return ResponseEntity.noContent().build();
    }

    @Override
    @PostMapping("/repayment/request")
    public ResponseEntity<Void> repaymentRequest(@AuthenticationPrincipal LoginUser loginUser,
                                                 @RequestBody RepaymentRequest repaymentRequest) {

        log.info("{ paper repayment rq : user_%d, paper_%d }".formatted(loginUser.id(), repaymentRequest.paperId()));

        promissoryPaperService.repayment(loginUser, repaymentRequest);

        return ResponseEntity.noContent().build();
    }

    @Override
    @PostMapping("/repayment/cancel")
    public ResponseEntity<Void> repaymentCancel(@AuthenticationPrincipal LoginUser loginUser,
                                                @RequestBody RepaymentCancelRequest repaymentCancelRequest) {

        log.info("{ paper repayment cancel rq : user_%d }".formatted(loginUser.id()));
        log.info("{ cancel target : paper_%d, history_%d }".formatted(repaymentCancelRequest.paperId(), repaymentCancelRequest.historyId()));

        promissoryPaperService.cancelRepayment(loginUser, repaymentCancelRequest);

        return ResponseEntity.noContent().build();
    }

    @Override
    @PutMapping("/refuse/{id}")
    public ResponseEntity<Void> refuse(@AuthenticationPrincipal LoginUser loginUser,
                                       @PathVariable(name = "id") Long paperId) {

        log.info("{ paper refuse rq : user_%d, paper_%d }".formatted(loginUser.id(), paperId));

        promissoryPaperService.refuse(loginUser, paperId);

        return ResponseEntity.noContent().build();
    }

    @Override
    @PostMapping("/reload")
    public ResponseEntity<Void> reload(@AuthenticationPrincipal LoginUser loginUser) {

        log.info("{ paper reload rq : user_%d }".formatted(loginUser.id()));

        promissoryPaperService.reload(loginUser);

        return ResponseEntity.noContent().build();
    }

    @Override
    @PutMapping("/hide/{id}")
    public ResponseEntity<Void> hide(@AuthenticationPrincipal LoginUser loginUser,
                                     @PathVariable(name = "id") Long paperId) {

        log.info("{ paper hide rq : user_%d, paper_%d }".formatted(loginUser.id(), paperId));

        promissoryPaperService.hide(loginUser, paperId);

        return ResponseEntity.noContent().build();
    }
}
