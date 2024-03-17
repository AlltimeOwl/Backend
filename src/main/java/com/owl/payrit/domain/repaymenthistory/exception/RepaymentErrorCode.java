package com.owl.payrit.domain.repaymenthistory.exception;

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
public enum RepaymentErrorCode implements BaseErrorCode {

    //Repayment 400 Error
    REPAYMENT_STATUS_ERROR(BAD_REQUEST, "REPAYMENT_400_1", "상환은 차용증 작성이 완료되었을 때만 가능합니다."),

    @ExplainError("상환 기록은 차용증에 작성된 상환 시작일과 상환 마감일의 사이에만 작성이 가능합니다.")
    REPAYMENT_NOT_VALID_DATE(BAD_REQUEST, "REPAYMENT_400_2", "상환 기록 작성일자가 유효하지 않습니다."),

    REPAYMENT_AMOUNT_OVER(BAD_REQUEST, "REPAYMENT_400_3", "상환 기록액이 남은 상환액을 초과합니다."),

    //Repayment 403 Error
    REPAYMENT_ONLY_ACCESS_CREDITOR(FORBIDDEN, "REPAYMENT_403_1", "상환 기록은 채권자만 작성, 삭제가 가능합니다."),

    //Repayment 404 Error
    REPAYMENT_HISTORY_NOT_FOUND(NOT_FOUND, "REPAYMENT_404_1", "상환 기록이 존재하지 않습니다.");

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
