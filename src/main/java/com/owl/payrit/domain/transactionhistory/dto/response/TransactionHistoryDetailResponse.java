package com.owl.payrit.domain.transactionhistory.dto.response;

import com.owl.payrit.domain.transactionhistory.entity.TransactionHistory;

import java.time.LocalDateTime;

public record TransactionHistoryDetailResponse(

        Long historyId,
        LocalDateTime transactionDate,
        String approvalNumber,
        String transactionType,
        long amount

        //TODO: 결제 구분 - 일시불?
) {
    public TransactionHistoryDetailResponse(TransactionHistory transactionHistory) {
        this(
                transactionHistory.getId(),
                transactionHistory.getTransactionDate(),
                transactionHistory.getApprovalNumber(),
                transactionHistory.getTransactionType(),
                transactionHistory.getAmount()
        );
    }
}
