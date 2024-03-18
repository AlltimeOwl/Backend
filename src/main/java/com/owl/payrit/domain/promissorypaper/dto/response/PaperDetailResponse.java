package com.owl.payrit.domain.promissorypaper.dto.response;

import com.owl.payrit.domain.promissorypaper.entity.PromissoryPaper;
import com.owl.payrit.domain.repaymenthistory.dto.RepaymentHistoryDto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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
        List<RepaymentHistoryDto> repaymentHistories
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
                promissoryPaper.getRepaymentHistory().stream()
                        .map(history -> new RepaymentHistoryDto(history.getId(), history.getRepaymentDate(),
                                history.getRepaymentAmount()))
                        .collect(Collectors.toList())
        );
    }
}
