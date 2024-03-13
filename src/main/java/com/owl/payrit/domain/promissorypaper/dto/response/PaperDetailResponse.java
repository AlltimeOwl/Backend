package com.owl.payrit.domain.promissorypaper.dto.response;

import com.owl.payrit.domain.promissorypaper.entity.PromissoryPaper;
import com.owl.payrit.domain.repaymenthistory.entity.RepaymentHistory;

import java.time.LocalDate;
import java.util.List;

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
        String specialConditions,
        List<RepaymentHistory> repaymentHistories
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
                promissoryPaper.getSpecialConditions(),
                promissoryPaper.getRepaymentHistory()
        );
    }
}
