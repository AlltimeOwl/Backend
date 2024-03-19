package com.owl.payrit.domain.transactionhistory.dto.response;

import com.owl.payrit.domain.transactionhistory.entity.TransactionHistory;

import java.time.LocalDate;

public record TransactionHistoryListResponse(

        LocalDate transactionDate,
        long amount,
        String transactionType,
        boolean isSuccess
) {

    public TransactionHistoryListResponse(TransactionHistory transactionHistory, LocalDate transactionDate) {
        this(
                transactionDate,
                transactionHistory.getAmount(),
                transactionHistory.getTransactionType(),
                transactionHistory.isSuccess()
        );
    }
}
