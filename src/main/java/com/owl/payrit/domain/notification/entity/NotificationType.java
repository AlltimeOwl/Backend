package com.owl.payrit.domain.notification.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationType {
    GENERAL("페이릿"),
    APPROVAL_REQUEST("승인요청 알림"),
    MODIFY_REQUEST("수정요청 알림"),
    PAYMENT_REQUEST("결제요청 알림"),
    REPAYMENT_REQUEST("상환요청 알림"),
    REPAYMENT_REMINDER("상환재촉 알림");

    private final String description;
}
