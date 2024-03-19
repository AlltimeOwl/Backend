package com.owl.payrit.domain.memo.dto.request;

import com.owl.payrit.domain.memo.entity.Memo;
import com.owl.payrit.domain.promissorypaper.entity.PromissoryPaper;

public record MemoWriteRequest(
    String content
) {

    public Memo toEntity(PromissoryPaper promissoryPaper, Long memberId) {
        return Memo.builder()
                   .promissoryPaper(promissoryPaper)
                   .content(content)
                   .memberId(memberId)
                   .build();
    }
}
