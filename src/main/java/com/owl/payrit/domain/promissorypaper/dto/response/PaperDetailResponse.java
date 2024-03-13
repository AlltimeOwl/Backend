package com.owl.payrit.domain.promissorypaper.dto.response;

import com.owl.payrit.domain.promissorypaper.entity.PromissoryPaper;

import java.time.LocalDate;

public record PaperDetailResponse(

        String paperUrl,
        long amount,
        long remainingAmount,
        float interestRate,
        double repaymentRate,
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
                promissoryPaper.getRemainingAmount(),
                promissoryPaper.getInterestRate(),
                repaymentRate,
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
