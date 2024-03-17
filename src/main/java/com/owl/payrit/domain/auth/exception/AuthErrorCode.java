package com.owl.payrit.domain.auth.exception;

import com.owl.payrit.global.exception.BaseErrorCode;
import com.owl.payrit.global.exception.ErrorReason;
import com.owl.payrit.global.swagger.annotation.ExplainError;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Field;
import java.util.Objects;

import static com.owl.payrit.global.consts.PayritStatic.BAD_REQUEST;
import static com.owl.payrit.global.consts.PayritStatic.FORBIDDEN;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements BaseErrorCode {

    //OAUTH Error
    IMPROPER_OAUTH_INFORMATION(BAD_REQUEST, "OAUTH_400_1", "올바른 OAUTH 정보가 아닙니다."),
    NOT_AUTHORIZED_MEMBER(FORBIDDEN, "OAUTH_403_1", "인증되지 않은 유저입니다.");

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
