package com.owl.payrit.domain.promissorypaper.dto.response;

import com.owl.payrit.domain.promissorypaper.entity.PaperRole;
import com.owl.payrit.domain.promissorypaper.entity.PaperStatus;
import com.owl.payrit.domain.promissorypaper.entity.PromissoryPaper;

import java.time.LocalDate;

public record PaperListResponse(
        PaperRole paperRole,
        LocalDate repaymentStartDate,
        LocalDate repaymentEndDate,
        long amount,
        PaperStatus paperStatus,
        String peerName,
        long dueDate
) {
    public PaperListResponse(PromissoryPaper promissoryPaper, PaperRole paperRole, String peerName, long dueDate) {
        this(
                paperRole,
                promissoryPaper.getRepaymentStartDate(),
                promissoryPaper.getRepaymentEndDate(),
                promissoryPaper.getAmount(),
                promissoryPaper.getPaperStatus(),
                peerName,
                dueDate
        );
    }
}