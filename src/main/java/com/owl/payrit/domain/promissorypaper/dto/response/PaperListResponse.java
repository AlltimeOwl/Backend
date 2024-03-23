package com.owl.payrit.domain.promissorypaper.dto.response;

import com.owl.payrit.domain.promissorypaper.entity.PaperRole;
import com.owl.payrit.domain.promissorypaper.entity.PaperStatus;
import com.owl.payrit.domain.promissorypaper.entity.PromissoryPaper;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record PaperListResponse(

        @Schema(description = "차용증 아이디")
        Long paperId,

        @Schema(description = "차용증에 대한 로그인한 회원의 역할")
        PaperRole paperRole,

        @Schema(description = "체결일(작성일)")
        LocalDate transactionDate,

        @Schema(description = "상환 시작일")
        LocalDate repaymentStartDate,

        @Schema(description = "상환 마감일")
        LocalDate repaymentEndDate,

        @Schema(description = "총액(원금 + 이자)")
        long amount,

        @Schema(description = "차용증 상태")
        PaperStatus paperStatus,

        @Schema(description = "상대방 이름")
        String peerName,

        @Schema(description = "남은 일 수")
        long dueDate,

        @Schema(description = "상환율")
        double repaymentRate,

        @Schema(description = "작성자 여부")
        boolean isWriter
) {
    public PaperListResponse(PromissoryPaper promissoryPaper, PaperRole paperRole,
                             String peerName, long dueDate, double repaymentRate, boolean isWriter) {
        this(
                promissoryPaper.getId(),
                paperRole,
                promissoryPaper.getTransactionDate(),
                promissoryPaper.getRepaymentStartDate(),
                promissoryPaper.getRepaymentEndDate(),
                promissoryPaper.getAmount(),
                promissoryPaper.getPaperStatus(),
                peerName,
                dueDate,
                repaymentRate,
                isWriter
        );
    }
}