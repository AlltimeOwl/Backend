package com.owl.payrit.domain.transactionhistory.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransactionType {

    PAPER_TRANSACTION("차용증 결제 대금"),
    NOTIFICATION_TRANSACTION("알림 발송 대금");

    private final String content;
}
