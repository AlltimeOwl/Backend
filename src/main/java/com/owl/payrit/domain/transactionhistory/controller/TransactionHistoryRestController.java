package com.owl.payrit.domain.transactionhistory.controller;

import com.owl.payrit.domain.auth.dto.response.LoginUser;
import com.owl.payrit.domain.transactionhistory.dto.request.TransactionHistorySaveRequest;
import com.owl.payrit.domain.transactionhistory.dto.response.TransactionHistoryDetailResponse;
import com.owl.payrit.domain.transactionhistory.dto.response.TransactionHistoryListResponse;
import com.owl.payrit.domain.transactionhistory.service.TransactionHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transaction")
public class TransactionHistoryRestController implements TransactionHistoryApiDocs {

    private final TransactionHistoryService transactionHistoryService;

    @Override
    @PostMapping("/save")
    public ResponseEntity<Void> save(@AuthenticationPrincipal LoginUser loginUser,
                                     @RequestBody TransactionHistorySaveRequest transactionHistorySaveRequest) {

        log.info(transactionHistorySaveRequest.toString());

        transactionHistoryService.saveHistory(loginUser, transactionHistorySaveRequest);

        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/detail/{id}")
    public ResponseEntity<TransactionHistoryDetailResponse> detail(@AuthenticationPrincipal LoginUser loginUser,
                                                                   @PathVariable(value = "id") Long id) {

        log.info("{user id : %d, history id : %d }".formatted(loginUser.id(), id));

        TransactionHistoryDetailResponse detailResponse = transactionHistoryService.getDetail(loginUser, id);

        return ResponseEntity.ok().body(detailResponse);
    }

    @Override
    @GetMapping("/list")
    public ResponseEntity<List<TransactionHistoryListResponse>> list(@AuthenticationPrincipal LoginUser loginUser) {

        log.info("{user id : %d }".formatted(loginUser.id()));

        List<TransactionHistoryListResponse> listResponses = transactionHistoryService.getListResponses(loginUser);

        return ResponseEntity.ok().body(listResponses);
    }

}
