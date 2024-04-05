package com.owl.payrit.domain.transactionhistory.exception;

import com.owl.payrit.global.exception.BaseErrorCode;
import com.owl.payrit.global.exception.ErrorReason;
import com.owl.payrit.global.swagger.annotation.ExplainError;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Field;
import java.util.Objects;

import static com.owl.payrit.global.consts.PayritStatic.*;

@Getter
@AllArgsConstructor
public enum TransactionHistoryErrorCode implements BaseErrorCode {

    //TransactionHistory 400 Error
    TRANSACTION_BAD_COST(BAD_REQUEST, "TRANSACTION_400_1", "결제 금액이 올바르지 않습니다."),
    TRANSACTION_BAD_DATE(BAD_REQUEST, "TRANSACTION_400_2", "결제 시기가 올바르지 않습니다."),
    PAYMENT_BAD_TYPE(BAD_REQUEST, "PAYMENT_400_1", "결제 유형이 올바르지 않습니다."),

    //TransactionHistory 403 Error
    TRANSACTION_ONLY_WRITER(FORBIDDEN, "TRANSACTION_403_1", "결제는 첫 작성자만 가능합니다."),
    TRANSACTION_FORBIDDEN(FORBIDDEN, "TRANSACTION_403_2", "결제 내역 조회 권한이 없습니다."),

    //TransactionHistory 404 Error
    TRANSACTION_NOT_FOUND(NOT_FOUND, "TRANSACTION_404_1", "결제 내역을 찾을 수 없습니다."),

    //TransactionHistory 409 Error
    TRANSACTION_APPROVAL_NUM_CONFLICT(CONFLICT, "TRANSACTION_409_1",
            "중복된 승인 코드가 사용되었습니다. 다시 시도해 주세요."),
    TRANSACTION_ORDER_NUM_CONFLICT(CONFLICT, "TRANSACTION_409_2", "중복된 주문 코드가 사용되었습니다. 다시 시도해 주세요.");

    private Integer status;
    private String code;
    private String reason;

    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.builder()
                .reason(reason)
                .code(code)
                .status(status)
                .build();
    }

    @Override
    public String getExplainError() throws NoSuchFieldException {
        Field field = this.getClass().getField(this.name());
        ExplainError annotation = field.getAnnotation(ExplainError.class);
        return Objects.nonNull(annotation) ? annotation.value() : this.getReason();
    }
}
