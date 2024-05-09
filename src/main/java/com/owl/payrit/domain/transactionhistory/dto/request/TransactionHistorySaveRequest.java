package com.owl.payrit.domain.transactionhistory.dto.request;

import com.owl.payrit.domain.transactionhistory.entity.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record TransactionHistorySaveRequest(

        @Schema(description = "결제가 진행된 차용증 id")
        Long paperId,

        @Schema(description = "결제 시각")
        LocalDateTime transactionDate,

        @Schema(description = "결제 금액")
        long amount,

        @Schema(description = "결제 종류")
        TransactionType transactionType,

        @Schema(description = "imp_ 로 시작하는 포트원 주문번호")
        String impUid,

        @Schema(description = "PR_ 로 시작하는 내부 주문번호")
        String merchantUid,

        @Schema(description = "결제 성공 여부")
        boolean isSuccess
) {
}
