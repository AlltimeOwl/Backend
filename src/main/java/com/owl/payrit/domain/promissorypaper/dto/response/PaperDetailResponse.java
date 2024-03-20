package com.owl.payrit.domain.promissorypaper.dto.response;

import com.owl.payrit.domain.promissorypaper.entity.PromissoryPaper;
import com.owl.payrit.domain.repaymenthistory.dto.RepaymentHistoryDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public record PaperDetailResponse(

        @Schema(description = "차용증 id")
        Long paperId,

        @Schema(description = "저장소 URL")
        String paperUrl,

        @Schema(description = "총액(원금 + 이자)")
        long amount,

        @Schema(description = "남은 금액(총액 - 일부상환액)")
        long remainingAmount,

        @Schema(description = "이자율")
        float interestRate,

        @Schema(description = "이자 지급일")
        long interestPaymentDate,

        @Schema(description = "상환 진행률")
        double repaymentRate,

        @Schema(description = "상환 시작일")
        LocalDate repaymentStartDate,

        @Schema(description = "상환 마감일")
        LocalDate repaymentEndDate,

        @Schema(description = "남은 일 수")
        long dueDate,

        @Schema(description = "채권자 이름")
        String creditorName,

        @Schema(description = "채권자 전화번호")
        String creditorPhoneNumber,

        @Schema(description = "채권자 주소")
        String creditorAddress,

        @Schema(description = "채무자 이름")
        String debtorName,

        @Schema(description = "채무자 전화번호")
        String debtorPhoneNumber,

        @Schema(description = "채무자 주소")
        String debtorAddress,

        @Schema(description = "특약사항")
        String specialConditions,

        @Schema(description = "상환내역 객체 리스트")
        List<RepaymentHistoryDto> repaymentHistories
) {
    public PaperDetailResponse(PromissoryPaper promissoryPaper, double repaymentRate, long dueDate) {
        this(
                promissoryPaper.getId(),
                promissoryPaper.getStorageUrl(),
                promissoryPaper.getAmount(),
                promissoryPaper.getRemainingAmount(),
                promissoryPaper.getInterestRate(),
                promissoryPaper.getInterestPaymentDate(),
                repaymentRate,
                promissoryPaper.getRepaymentStartDate(),
                promissoryPaper.getRepaymentEndDate(),
                dueDate,
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
