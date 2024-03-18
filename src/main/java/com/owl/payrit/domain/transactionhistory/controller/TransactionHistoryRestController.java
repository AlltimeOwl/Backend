package com.owl.payrit.domain.transactionhistory.controller;

import com.owl.payrit.domain.auth.dto.response.LoginUser;
import com.owl.payrit.domain.transactionhistory.dto.request.TransactionHistorySaveRequest;
import com.owl.payrit.domain.transactionhistory.dto.response.TransactionHistoryDetailResponse;
import com.owl.payrit.domain.transactionhistory.service.TransactionHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transaction")
public class TransactionHistoryRestController {

    private final TransactionHistoryService transactionHistoryService;

    @PostMapping("/save")
    public ResponseEntity<Void> save(@AuthenticationPrincipal LoginUser loginUser,
                                     @RequestBody TransactionHistorySaveRequest transactionHistorySaveRequest) {

        log.info(transactionHistorySaveRequest.toString());

        transactionHistoryService.saveHistory(loginUser, transactionHistorySaveRequest);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<TransactionHistoryDetailResponse> detail(@AuthenticationPrincipal LoginUser loginUser,
                                                                   @PathVariable(value = "id") Long id) {

        log.info("{user id : %d, history id : %d }".formatted(loginUser.id(), id));

        TransactionHistoryDetailResponse detailResponse = transactionHistoryService.getDetail(loginUser, id);

        return ResponseEntity.ok().body(detailResponse);
    }

}
