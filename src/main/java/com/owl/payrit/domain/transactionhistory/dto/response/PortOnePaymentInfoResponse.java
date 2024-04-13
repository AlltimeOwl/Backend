package com.owl.payrit.domain.transactionhistory.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

public record PortOnePaymentInfoResponse(
        Integer code,
        String message,
        PaymentAnnotation response
) {
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record PaymentAnnotation(
            String impUid,
            String merchantUid,
            String payMethod,
            String channel,
            String pgProvider,
            String embPgProvider,
            String pgTid,
            String pgId,
            String escrow,
            String applyNum,
            String bankCode,
            String bankName,
            String cardCode,
            String cardName,
            String cardIssuerCode,
            String cardIssuerName,
            String cardPublisherCode,
            String cardPublisherName,
            String cardQuota,
            String cardNumber,
            int cardType,
            String vbankCode,
            String vbankName,
            String vbankNum,
            String vbankHolder,
            long vbankDate,
            long vbankIssuedAt,
            String name,
            int amount,
            int cancelAmount,
            String currency,
            String buyerName,
            String buyerEmail,
            String buyerTel,
            String buyerAddr,
            String buyerPostcode,
            String customData,
            String userAgent,
            String status,
            long startedAt,
            long paidAt,
            long failedAt,
            long cancelledAt,
            String failReason,
            String cancelReason,
            String receiptUrl,
            CancelHistory[] cancelHistory,
            String[] cancelReceiptUrls,
            boolean cashReceiptIssued,
            String customerUid,
            String customerUidUsage
    ) {
        public record CancelHistory(
                String pgTid,
                int amount,
                long cancelledAt,
                String reason,
                String cancellationId,
                String receiptUrl
        ) {}
    }
}