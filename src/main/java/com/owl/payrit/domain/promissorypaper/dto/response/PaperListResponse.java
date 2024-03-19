package com.owl.payrit.domain.promissorypaper.dto.response;

import com.owl.payrit.domain.promissorypaper.entity.PaperRole;
import com.owl.payrit.domain.promissorypaper.entity.PaperStatus;
import com.owl.payrit.domain.promissorypaper.entity.PromissoryPaper;

import java.time.LocalDate;

public record PaperListResponse(
        PaperRole paperRole,
        LocalDate transactionDate,
        LocalDate repaymentStartDate,
        LocalDate repaymentEndDate,
        long amount,
        PaperStatus paperStatus,
        String peerName,
        long dueDate,
        double repaymentRate
) {
    public PaperListResponse(PromissoryPaper promissoryPaper, PaperRole paperRole,
                             String peerName, long dueDate, double repaymentRate) {
        this(
                paperRole,
                promissoryPaper.getTransactionDate(),
                promissoryPaper.getRepaymentStartDate(),
                promissoryPaper.getRepaymentEndDate(),
                promissoryPaper.getAmount(),
                promissoryPaper.getPaperStatus(),
                peerName,
                dueDate,
                repaymentRate
        );
    }
}