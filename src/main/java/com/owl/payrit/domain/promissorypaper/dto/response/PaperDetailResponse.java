package com.owl.payrit.domain.promissorypaper.dto.response;

import com.owl.payrit.domain.promissorypaper.entity.PromissoryPaper;

import java.time.LocalDate;

public record PaperDetailResponse(
        //FIXME: 단순 Paper객체가 아닌 Response를 반환하는 것과의 차이점...?

        String paperUrl,
        long totalAmount,                   //원금 + 이자로 이루어진 총액 (차용 금액 X)
        float repaymentRate,                  //상환 비율({현재 상환액} / {원금 + 이자})
        long currentRepaymentAmount,
        LocalDate repaymentStartDate,       //FIXME: 거래 날짜 : repaymentStartDate or transactionDate
        LocalDate repaymentEndDate,
        String creditorName,
        String creditorPhoneNumber,
        String creditorAddress,
        String debtorName,
        String debtorPhoneNumber,
        String debtorAddress,
        String specialConditions
) {
    public PaperDetailResponse(PromissoryPaper promissoryPaper) {
        this(
                promissoryPaper.getStorageUrl(),
                promissoryPaper.getAmount(),    //FIXME: 금액 + 이자로 수정 필요
                promissoryPaper.getInterestRate(),
                promissoryPaper.getCurrentRepaymentAmount(),
                promissoryPaper.getRepaymentStartDate(),
                promissoryPaper.getRepaymentEndDate(),
                promissoryPaper.getCreditor().getName(),
                promissoryPaper.getCreditorPhoneNumber(),
                promissoryPaper.getCreditorAddress(),
                promissoryPaper.getDebtor().getName(),
                promissoryPaper.getDebtorPhoneNumber(),
                promissoryPaper.getDebtorAddress(),
                promissoryPaper.getSpecialConditions()
        );
    }
}
