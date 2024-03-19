package com.owl.payrit.domain.memo.dto.response;

import com.owl.payrit.domain.memo.entity.Memo;
import java.time.LocalDateTime;

public record MemoListResponse(
    Long id,
    String content,
    LocalDateTime createdAt
) {
    public MemoListResponse(Memo memo) {
        this (
            memo.getId(),
            memo.getContent(),
            memo.getCreatedAt()
        );
    }
}
