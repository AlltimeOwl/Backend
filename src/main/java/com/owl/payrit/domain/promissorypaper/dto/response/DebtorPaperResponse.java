package com.owl.payrit.domain.promissorypaper.dto.response;

import com.owl.payrit.domain.promissorypaper.entity.PromissoryPaper;

import java.time.LocalDate;

public record DebtorPaperResponse(
        long amount,
        LocalDate repaymentStartDate,
        LocalDate repaymentEndDate,
        String creditorName,
        int dueDate,
        String paperStatus
) {
    public DebtorPaperResponse(PromissoryPaper promissoryPaper) {
        this(
                promissoryPaper.getAmount(),
                promissoryPaper.getRepaymentStartDate(),
                promissoryPaper.getRepaymentEndDate(),
                promissoryPaper.getCreditor().getName(),    //TODO: 클래스 하나로 처리 가능할 것 같음. 고려 필요
                9999,                                       //TODO: DDAY 계산하는 로직을 어디에 작성하는 것이 맞는가?
                promissoryPaper.getPaperStatus().name()
        );
    }
}