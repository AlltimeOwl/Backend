package com.owl.payrit.domain.transactionhistory.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record PortOnePaymentCancelRequest(

        @Schema(description = "imp_로 시작하는 포트원 주문번호")
        String impUid,

        @Schema(description = "PR_ 로 시작하는 내부 주문번호")
        String merchantUid,

        @Schema(description = "취소 사유")
        String reason
) {
}
