package com.owl.payrit.domain.transactionhistory.dto.request;

import com.owl.payrit.domain.transactionhistory.entity.TransactionType;

import java.time.LocalDateTime;

public record TransactionHistorySaveRequest(

        Long paperId,
        LocalDateTime transactionDate,
        long amount,
        TransactionType transactionType,
        String impUid,
        String merchantUid,
        boolean isSuccess
) {
}
