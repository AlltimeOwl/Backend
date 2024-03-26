package com.owl.payrit.domain.promissorypaper.controller;

import com.owl.payrit.domain.auth.dto.response.LoginUser;
import com.owl.payrit.domain.promissorypaper.dto.request.PaperModifyRequest;
import com.owl.payrit.domain.promissorypaper.dto.request.PaperWriteRequest;
import com.owl.payrit.domain.promissorypaper.dto.response.PaperDetailResponse;
import com.owl.payrit.domain.promissorypaper.dto.response.PaperListResponse;
import com.owl.payrit.domain.promissorypaper.service.PromissoryPaperService;
import com.owl.payrit.domain.repaymenthistory.dto.request.RepaymentCancelRequest;
import com.owl.payrit.domain.repaymenthistory.dto.request.RepaymentRequest;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

        log.info(paperWriteRequest.toString());

        promissoryPaperService.writePaper(loginUser, paperWriteRequest, req);

        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<PaperDetailResponse> detail(@AuthenticationPrincipal LoginUser loginUser,
                                                      @PathVariable(value = "id") Long id) {

        log.info("request paper id : " + id);

        PaperDetailResponse paperDetailResponse = promissoryPaperService.getDetail(loginUser, id);

        return ResponseEntity.ok().body(paperDetailResponse);
    }

    @Override
    @GetMapping("/list")
    public ResponseEntity<List<PaperListResponse>> list(@AuthenticationPrincipal LoginUser loginUser) {

        log.info("request user id : " + loginUser.id());

        List<PaperListResponse> allListResponses = promissoryPaperService.getAllListResponse(loginUser);

        return ResponseEntity.ok().body(allListResponses);
    }

    @Override
    @PutMapping("/approve/accept/{id}")
    public ResponseEntity<Void> acceptPaper(@AuthenticationPrincipal LoginUser loginUser,
                                            @PathVariable(value = "id") Long paperId) {

        log.info("request user id : " + loginUser.id());

        promissoryPaperService.acceptPaper(loginUser, paperId);

        return ResponseEntity.noContent().build();
    }

    @Override
    @PostMapping("/modify/request")
    public ResponseEntity<Void> requestModify(@AuthenticationPrincipal LoginUser loginUser,
                                              @RequestBody PaperModifyRequest paperModifyRequest) {

        log.info("request user id : " + loginUser.id());
        log.info("contents : " + paperModifyRequest.contents());

        promissoryPaperService.sendModifyRequest(loginUser, paperModifyRequest);

        return ResponseEntity.noContent().build();
    }

    @Override
    @PutMapping("/modify/accept/{id}")
    public ResponseEntity<Void> modifying(@AuthenticationPrincipal LoginUser loginUser,
                                          @PathVariable(value = "id") Long id,
                                          @RequestBody PaperWriteRequest paperWriteRequest) {

        promissoryPaperService.modifyingPaper(loginUser, id, paperWriteRequest);

        return ResponseEntity.noContent().build();
    }

    @Override
    @PostMapping("/repayment/request")
    public ResponseEntity<Void> repaymentRequest(@AuthenticationPrincipal LoginUser loginUser,
                                                 @RequestBody RepaymentRequest repaymentRequest) {

        promissoryPaperService.repayment(loginUser, repaymentRequest);

        return ResponseEntity.noContent().build();
    }

    @Override
    @PostMapping("/repayment/cancel")
    public ResponseEntity<Void> repaymentCancel(@AuthenticationPrincipal LoginUser loginUser,
                                                @RequestBody RepaymentCancelRequest repaymentCancelRequest) {

        promissoryPaperService.cancelRepayment(loginUser, repaymentCancelRequest);

        return ResponseEntity.noContent().build();
    }
}
