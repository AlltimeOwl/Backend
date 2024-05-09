package com.owl.payrit.domain.transactionhistory.dto.request;

import com.owl.payrit.domain.transactionhistory.entity.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;

public record TransactionInfoRequest(

        @Schema(description = "결제를 진행할 차용증의 id값 입니다.")
        Long paperId,
        
        @Schema(description = "결제 형식 입니다. (차용증 대금 결제 or 알림 결제)")
        TransactionType transactionType
) {
}
