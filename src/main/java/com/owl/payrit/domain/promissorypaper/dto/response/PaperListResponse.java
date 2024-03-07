package com.owl.payrit.domain.promissorypaper.dto.response;

import com.owl.payrit.domain.promissorypaper.entity.PromissoryPaper;

import java.time.LocalDate;
import java.util.List;

public record PaperListResponse(
        LocalDate repaymentStartDate,
        LocalDate repaymentEndDate,
        long amount,
        String paperStatus,
        String peerName,
        long dueDate
) {
    public PaperListResponse(PromissoryPaper promissoryPaper, String peerName, long dueDate) {
        this(
                promissoryPaper.getRepaymentStartDate(),
                promissoryPaper.getRepaymentEndDate(),
                promissoryPaper.getAmount(),
                promissoryPaper.getPaperStatus().name(),
                peerName,
                dueDate
        );
    }
}
