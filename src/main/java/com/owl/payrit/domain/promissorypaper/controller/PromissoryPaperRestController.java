package com.owl.payrit.domain.promissorypaper.controller;

import com.owl.payrit.domain.auth.dto.response.LoginUser;
import com.owl.payrit.domain.promissorypaper.dto.request.PaperModifyRequest;
import com.owl.payrit.domain.promissorypaper.dto.request.PaperWriteRequest;
import com.owl.payrit.domain.promissorypaper.dto.response.PaperDetailResponse;
import com.owl.payrit.domain.promissorypaper.dto.response.PaperListResponse;
import com.owl.payrit.domain.promissorypaper.service.PromissoryPaperService;
import com.owl.payrit.domain.repaymenthistory.dto.request.RepaymentCancelRequest;
import com.owl.payrit.domain.repaymenthistory.dto.request.RepaymentRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/paper")
public class PromissoryPaperRestController implements PromissoryPaperApiDocs{

    private final PromissoryPaperService promissoryPaperService;

    @Override
    @PostMapping("/write")
    public ResponseEntity<String> write(@AuthenticationPrincipal LoginUser loginUser,
                                        @RequestBody PaperWriteRequest paperWriteRequest) {

        log.info(paperWriteRequest.toString());

        promissoryPaperService.writePaper(loginUser, paperWriteRequest);

        return ResponseEntity.ok().body("write");
    }

    @Override
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

    @PostMapping("/modify/request")
    public ResponseEntity<String> requestModify(@AuthenticationPrincipal LoginUser loginUser,
                                                @RequestBody PaperModifyRequest paperModifyRequest) {

        log.info("request user id : " + loginUser.id());
        log.info("contents : " + paperModifyRequest.contents());

        promissoryPaperService.sendModifyRequest(loginUser, paperModifyRequest);

        return ResponseEntity.ok().body("modify request : %s".formatted(paperModifyRequest.contents()));
    }

    @PutMapping("/modify/accept/{id}")
    public ResponseEntity<String> modifying(@AuthenticationPrincipal LoginUser loginUser,
                                            @PathVariable(value = "id") Long paperId,
                                            @RequestBody PaperWriteRequest paperWriteRequest) {

        //FIXME: 수정시에도 PaperWriteRequest를 요청?
        promissoryPaperService.modifyingPaper(loginUser, paperId, paperWriteRequest);

        return ResponseEntity.ok().body("modify success");
    }

    @PostMapping("/repayment/request")
    public ResponseEntity<String> repaymentRequest(@AuthenticationPrincipal LoginUser loginUser,
                                                   @RequestBody RepaymentRequest repaymentRequest) {

        promissoryPaperService.repayment(loginUser, repaymentRequest);

        return ResponseEntity.ok().body("repayment success");
    }

    @PostMapping("/repayment/cancel")
    public ResponseEntity<String> repaymentCancel(@AuthenticationPrincipal LoginUser loginUser,
                                                  @RequestBody RepaymentCancelRequest repaymentCancelRequest) {

        promissoryPaperService.cancelRepayment(loginUser, repaymentCancelRequest);

        return ResponseEntity.ok().body("repayment canceled");
    }
}
