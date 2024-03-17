package com.owl.payrit.domain.promissorypaper.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record PaperModifyRequest(
        
        @Schema(description = "차용증 id")
        Long paperId,

        @Schema(description = "수정 요청시 함께 보낼 메세지")
        String contents
) {
}
