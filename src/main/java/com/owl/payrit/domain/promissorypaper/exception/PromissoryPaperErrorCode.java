package com.owl.payrit.domain.promissorypaper.exception;

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
public enum PromissoryPaperErrorCode implements BaseErrorCode {

    //PromissoryPaper Error
    PAPER_LIST_EXCEPTION(BAD_REQUEST, "PAPER_400_1", "차용증 리스트 조회가 올바르지 않습니다."),
    PAPER_CANNOT_ACCEPT_SELF(BAD_REQUEST, "PAPER_400_2", "작성자는 승인할 수 없습니다."),
    PAPER_STATUS_NOT_VALID(BAD_REQUEST, "PAPER_400_3", "차용증에 대한 요청 단계가 올바르지 않습니다."),
    PAPER_MATCHING_FAILED(BAD_REQUEST, "PAPER_400_4", "차용증 정보와 회원의 정보가 일치하지 않습니다."),
    PAPER_DATA_BAD_REQUEST(BAD_REQUEST, "PAPER_400_5", "차용증 데이터가 올바르지 않습니다."),

    PAPER_NOT_FOUND(UNAUTHORIZED, "PAPER_404_1", "차용증 정보가 존재하지 않습니다."),

    PAPER_IS_NOT_MINE(FORBIDDEN, "PAPER_403_1", "차용증 접근 권한이 없습니다."),
    PAPER_WRITER_CAN_MODIFY(FORBIDDEN, "PAPER_403_2", "첫 작성자만 수정이 가능합니다.");

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
