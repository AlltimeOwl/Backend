package com.owl.payrit.domain.promissorypaper.dto.response;

import com.owl.payrit.domain.promissorypaper.entity.PromissoryPaper;

import java.time.LocalDate;

public record CreditorPaperResponse(
        long amount,
        LocalDate repaymentStartDate,
        LocalDate repaymentEndDate,
        String debtorName,
        int dueDate,
        String paperStatus
) {
    public CreditorPaperResponse(PromissoryPaper promissoryPaper) {
        this(
                promissoryPaper.getAmount(),
                promissoryPaper.getRepaymentStartDate(),
                promissoryPaper.getRepaymentEndDate(),
                promissoryPaper.getDebtor().getName(),      //TODO: 상대가 아직 가입하기 전일 때 이름을 가져오려면?
                9999,                                       //TODO: DDAY 계산하는 로직을 어디에 작성하는 것이 맞는가?
                promissoryPaper.getPaperStatus().name()
        );
    }
}
