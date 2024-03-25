package com.owl.payrit.domain.docsinfo.exception;

import com.owl.payrit.global.exception.BaseErrorCode;
import com.owl.payrit.global.exception.ErrorReason;
import com.owl.payrit.global.swagger.annotation.ExplainError;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Field;
import java.util.Objects;

import static com.owl.payrit.global.consts.PayritStatic.BAD_REQUEST;

@Getter
@AllArgsConstructor
public enum DocsInfoErrorCode implements BaseErrorCode {

    //DocsInfo 400 Error
    DOCS_BAD_REQUEST(BAD_REQUEST ,"DOCS_400_1", "문서 정보 요청이 잘못되었습니다.");

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
