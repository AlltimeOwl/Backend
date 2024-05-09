package com.owl.payrit.domain.promissorypaper.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaperStatus {
    //동의 대기중, 수정중, 결제 필요, 작성 완료, 만료됨, 거절됨
    WAITING_AGREE("승인 대기중"),
    MODIFYING("수정 진행중"),
    PAYMENT_REQUIRED("결제 필요"),
    COMPLETE_WRITING("작성 완료됨"),
    EXPIRED("만료됨"),
    REFUSED("거절됨");

    private final String state;
}