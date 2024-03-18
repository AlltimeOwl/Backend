package com.owl.payrit.domain.transactionhistory.dto.request;

import java.time.LocalDateTime;

public record TransactionHistorySaveRequest(

        LocalDateTime transactionDate,
        long amount,
        String contents,
        String transactionType,
        String approvalNumber,
        String orderNumber,
        boolean isSuccess
) {
}
