package com.owl.payrit.domain.promissorypaper.dto.response;

import com.owl.payrit.domain.promissorypaper.entity.PromissoryPaper;

import java.time.LocalDate;

public record PaperDetailResponse(
        //FIXME: 단순 Paper객체가 아닌 Response를 반환하는 것과의 차이점...?

        String paperUrl,
        long totalAmount,
        float interestRate,
        double repaymentRate,
        long currentRepaymentAmount,
        LocalDate repaymentStartDate,
        LocalDate repaymentEndDate,
        String creditorName,
        String creditorPhoneNumber,
        String creditorAddress,
        String debtorName,
        String debtorPhoneNumber,
        String debtorAddress,
        String specialConditions
) {
    public PaperDetailResponse(PromissoryPaper promissoryPaper, double repaymentRate) {
        this(
                promissoryPaper.getStorageUrl(),
                promissoryPaper.getAmount(),
                promissoryPaper.getInterestRate(),
                repaymentRate,
                promissoryPaper.getRepaymentAmount(),
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
