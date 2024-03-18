package com.owl.payrit.domain.transactionhistory.controller;

import com.owl.payrit.domain.auth.dto.response.LoginUser;
import com.owl.payrit.domain.transactionhistory.dto.request.TransactionHistorySaveRequest;
import com.owl.payrit.domain.transactionhistory.service.TransactionHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transaction")
public class TransactionHistoryRestController {

    private final TransactionHistoryService transactionHistoryService;

    @PostMapping("/transaction/save")
    public ResponseEntity<Void> save(@AuthenticationPrincipal LoginUser loginUser,
                                     TransactionHistorySaveRequest transactionHistorySaveRequest) {

        log.info(transactionHistorySaveRequest.toString());

        transactionHistoryService.saveHistory(loginUser, transactionHistorySaveRequest);

        return ResponseEntity.noContent().build();
    }

}
