package com.owl.payrit.domain.memo.exception;

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
    PAPER_LIST_EXCEPTION(UNAUTHORIZED, "MEMO_401_1", "차용증 작성자만 메모를 작성할 수 있습니다."),;

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
