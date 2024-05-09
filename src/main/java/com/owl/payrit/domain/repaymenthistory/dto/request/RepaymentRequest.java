package com.owl.payrit.domain.repaymenthistory.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record RepaymentRequest(

        @Schema(description = "상환을 기록할 차용증 id")
        long paperId,

        @Schema(description = "상환 기록일")
        LocalDate repaymentDate,

        @Schema(description = "상환액")
        long repaymentAmount
) {
}
