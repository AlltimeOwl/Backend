package com.owl.payrit.domain.repaymenthistory.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record RepaymentCancelRequest(
        
        @Schema(description = "상환이 기록되었던 차용증 id")
        Long paperId,

        @Schema(description = "상환 기록의 id")
        Long historyId
) {
}
