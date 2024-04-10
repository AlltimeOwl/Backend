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

    //PromissoryPaper 400 Error
    PAPER_LIST_EXCEPTION(BAD_REQUEST, "PAPER_400_1", "차용증 리스트 조회가 올바르지 않습니다."),
    PAPER_CANNOT_ACCEPT_SELF(BAD_REQUEST, "PAPER_400_2", "작성자와 승인자가 같을 수 없습니다."),

    @ExplainError("승인과 수정요청은 승인 대기중인 상태에만 가능합니다. 결제 필요, 수정중 등의 상태에서는 불가합니다.")
    PAPER_STATUS_IS_NOT_WAITING(BAD_REQUEST, "PAPER_400_3", "승인과 수정 요청은 승인 대기상태에만 가능합니다."),

    @ExplainError("빌려준 회원은 CREDITOR, 빌린 회원은 DEBTOR와 데이터가 일치해야 합니다.")
    PAPER_MATCHING_FAILED(BAD_REQUEST, "PAPER_400_4", "차용증 정보와 회원의 정보가 일치하지 않습니다."),

    PAPER_INTEREST_RATE_NOT_VALID(BAD_REQUEST, "PAPER_400_5", "이자율은 0%부터 20% 까지만 입력할 수 있습니다."),
    PAPER_REPAYMENT_START_DATE_NOT_VALID(BAD_REQUEST, "PAPER_400_6", "상환 시작일은 과거로 설정할 수 없습니다."),
    PAPER_REPAYMENT_END_DATE_NOT_VALID(BAD_REQUEST, "PAPER_400_7", "상환 마감일은 시작일보다 과거일 수 없습니다."),

    @ExplainError("차용증의 수정은 상대방이 요청 했을 경우에만 가능합니다.")
    PAPER_STATUS_IS_NOT_MODIFYING(BAD_REQUEST, "PAPER_400_8", "차용증의 수정 요청을 받지 않았습니다."),
    NEED_AUTHENTICATION(BAD_REQUEST, "PAPER_400_9", "본인인증을 완료한 사용자만 차용증 작성이 가능합니다."),
    REFUSE_NEED_WAITING_STATUS(BAD_REQUEST, "PAPER_400_10", "승인 대기 상태에서만 거절이 가능합니다."),
    REFUSE_CANT_OTHER_PERSON(BAD_REQUEST, "PAPER_400_11", "거절은 요청을 받은 본인만 가능합니다."),

    //PromissoryPaper 403 Error
    @ExplainError("차용증은 채권자와 채무자에게만 접근 권한이 있습니다.")
    PAPER_IS_NOT_MINE(FORBIDDEN, "PAPER_403_1", "차용증 접근 권한이 없습니다."),

    @ExplainError("수정 요청은 작성 요청을 받은 사람만 가능합니다.")
    PAPER_CANNOT_REQUEST_MODIFY(FORBIDDEN, "PAPER_403,2", "수정을 요청할 권한이 없습니다."),

    @ExplainError("차용증의 수정은 첫 작성자만 할 수 있습니다.")
    PAPER_WRITER_CAN_MODIFY(FORBIDDEN, "PAPER_403_2", "수정을 진행할 권한이 없습니다."),

    //PromissoryPaper 404 Error
    PAPER_NOT_FOUND(NOT_FOUND, "PAPER_404_1", "차용증 정보가 존재하지 않습니다.");

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
