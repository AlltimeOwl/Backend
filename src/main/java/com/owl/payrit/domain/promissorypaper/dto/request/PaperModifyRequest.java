package com.owl.payrit.domain.promissorypaper.dto.request;

public record PaperModifyRequest(
        Long paperId,
        Long writerId,
        String contents
) {
}
