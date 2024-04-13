package com.owl.payrit.domain.transactionhistory.controller;

import com.owl.payrit.domain.auth.dto.response.LoginUser;
import com.owl.payrit.domain.transactionhistory.dto.request.PortOnePaymentCancelRequest;
import com.owl.payrit.domain.transactionhistory.dto.request.TransactionHistorySaveRequest;
import com.owl.payrit.domain.transactionhistory.dto.request.TransactionInfoRequest;
import com.owl.payrit.domain.transactionhistory.dto.response.PaymentInfoResponse;
import com.owl.payrit.domain.transactionhistory.dto.response.PortOnePaymentCancelResponse;
import com.owl.payrit.domain.transactionhistory.dto.response.TransactionHistoryDetailResponse;
import com.owl.payrit.domain.transactionhistory.dto.response.TransactionHistoryListResponse;
import com.owl.payrit.domain.transactionhistory.entity.TransactionType;
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
    @PostMapping("/paymentInfo/{id}/{transaction_type}")
    public ResponseEntity<PaymentInfoResponse> getPaymentInfo(@AuthenticationPrincipal LoginUser loginUser,
                                                              @PathVariable(name = "id") Long paperId,
                                                              @PathVariable(name = "transaction_type") TransactionType transactionType) {

        PaymentInfoResponse paymentInfo = transactionHistoryService.getPaymentInfo(loginUser.id(), paperId, transactionType);

        return ResponseEntity.ok().body(paymentInfo);
    }

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

    @Override
    @PostMapping("/dev/cancel/{secretKey}")
    public ResponseEntity<PortOnePaymentCancelResponse> cancelForDev(@AuthenticationPrincipal LoginUser loginUser,
                                                                     @PathVariable(name = "secretKey") String secretKey,
                                                                     @RequestBody PortOnePaymentCancelRequest request) {

        PortOnePaymentCancelResponse portOnePaymentCancelResponse =
                transactionHistoryService.cancelForDev(loginUser, secretKey, request);

        return ResponseEntity.ok().body(portOnePaymentCancelResponse);
    }
}
