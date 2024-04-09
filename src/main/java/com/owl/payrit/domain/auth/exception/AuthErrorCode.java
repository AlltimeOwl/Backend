package com.owl.payrit.domain.auth.exception;

import static com.owl.payrit.global.consts.PayritStatic.BAD_REQUEST;
import static com.owl.payrit.global.consts.PayritStatic.CONFLICT;
import static com.owl.payrit.global.consts.PayritStatic.FORBIDDEN;
import static com.owl.payrit.global.consts.PayritStatic.INTERNAL_SERVER;

import com.owl.payrit.global.exception.BaseErrorCode;
import com.owl.payrit.global.exception.ErrorReason;
import com.owl.payrit.global.swagger.annotation.ExplainError;
import java.lang.reflect.Field;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements BaseErrorCode {

    //OAUTH Error
    IMPROPER_OAUTH_INFORMATION(BAD_REQUEST, "OAUTH_400_1", "올바른 OAUTH 정보가 아닙니다."),
    NOT_AUTHORIZED_MEMBER(FORBIDDEN, "OAUTH_403_1", "인증되지 않은 유저입니다."),
    ALREADY_USER_AUTHENTICATED(CONFLICT, "OAUTH_409_1", "이미 인증된 사용자입니다."),
    IMMUTABLE_USER_AUTHENTICATED(CONFLICT, "OAUTH_409_1", "이미 인증된 계정이 존재합니다."),
    INTERNAL_SERVER_ERROR(INTERNAL_SERVER, "OAUTH_500_1", "OAUTH 서버 측 에러입니다. 다시 시도해주세요."),
    FILE_PATH_ERROR(INTERNAL_SERVER, "OAUTH_500_2", "Secret Key File 경로가 올바르지 않습니다.");

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
