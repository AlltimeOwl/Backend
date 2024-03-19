package com.owl.payrit.domain.memo.exception;

import static com.owl.payrit.global.consts.PayritStatic.NOT_FOUND;
import static com.owl.payrit.global.consts.PayritStatic.UNAUTHORIZED;

import com.owl.payrit.global.exception.BaseErrorCode;
import com.owl.payrit.global.exception.ErrorReason;
import com.owl.payrit.global.swagger.annotation.ExplainError;
import java.lang.reflect.Field;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemoErrorCode implements BaseErrorCode {
    MEMO_NOT_FOUND(NOT_FOUND, "MEMO_404_1", "메모가 존재하지 않습니다."),
    UNAUTHORIZED_MODIFY_MEMO(UNAUTHORIZED, "MEMO_401_1", "메모 수정 권한이 없습니다.")
    ;

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
