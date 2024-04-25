package com.owl.payrit.domain.promise.exception;

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
public enum PromiseErrorCode implements BaseErrorCode {

    // PROMISE 400 ERROR
    PROMISE_PARTICIPANTS_SIZE(BAD_REQUEST, "PROMISE_400_1", "참여자들의 이름과 전화번호 수가 일치하지 않습니다"),

    // PROMISE 403 ERROR
    PROMISE_IS_NOT_MINE(FORBIDDEN, "PROMISE_403_1", "약속 접근 권한이 없습니다."),

    // PROMISE 404 ERROR
    PROMISE_NOT_FOUND(NOT_FOUND, "PROMISE_404_1", "약속을 찾을 수 없습니다.");

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
