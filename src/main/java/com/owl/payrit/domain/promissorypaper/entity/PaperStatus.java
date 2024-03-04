package com.owl.payrit.domain.promissorypaper.entity;

public enum PaperStatus {
    //동의 대기중, 수정중, 결제 필요, 작성 완료, 만료됨
    WAITING_AGREE, MODIFYING, PAYMENT_REQUIRED,COMPLETE_WRITING, EXPIRED
}