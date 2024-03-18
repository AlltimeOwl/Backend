package com.owl.payrit.domain.member.exception;

import com.owl.payrit.global.exception.BaseErrorCode;
import com.owl.payrit.global.exception.ErrorReason;
import com.owl.payrit.global.swagger.annotation.ExplainError;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Field;
import java.util.Objects;

import static com.owl.payrit.global.consts.PayritStatic.NOT_FOUND;
import static com.owl.payrit.global.consts.PayritStatic.UNAUTHORIZED;

@Getter
@AllArgsConstructor
public enum MemberErrorCode implements BaseErrorCode {

    //Member Error
    NOT_AUTHORIZED_MEMBER(UNAUTHORIZED, "MEMBER_401_1", "본인인증이 완료되지 않은 회원입니다."),
    MEMBER_NOT_FOUND(NOT_FOUND, "MEMBER_404_1", "회원이 존재하지 않습니다.");

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
