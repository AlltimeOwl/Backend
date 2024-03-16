package com.owl.payrit.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    //Example Error
    EXAMPLE_EXCEPTION(HttpStatus.BAD_REQUEST, "EXAMPLE_400_1", "예시 에러입니다."),

    //OAUTH Error
    IMPROPER_OAUTH_INFORMATION(HttpStatus.BAD_REQUEST, "OAUTH_400_1", "올바른 OAUTH 정보가 아닙니다."),

    //Member Error
    NOT_AUTHORIZED_MEMBER(HttpStatus.UNAUTHORIZED, "MEMBER_401_1", "본인인증이 완료되지 않은 회원입니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER_404_1", "회원이 존재하지 않습니다."),

    //PromissoryPaper Error
    PAPER_LIST_EXCEPTION(HttpStatus.BAD_REQUEST, "PAPER_400_1", "차용증 리스트 조회가 올바르지 않습니다."),
    PAPER_CANNOT_ACCEPT_SELF(HttpStatus.BAD_REQUEST, "PAPER_400_2", "작성자는 승인할 수 없습니다."),
    PAPER_STATUS_NOT_VALID(HttpStatus.BAD_REQUEST, "PAPER_400_3", "차용증에 대한 요청 단계가 올바르지 않습니다."),
    PAPER_MATCHING_FAILED(HttpStatus.BAD_REQUEST, "PAPER_400_4", "차용증 정보와 회원의 정보가 일치하지 않습니다."),
    PAPER_DATA_BAD_REQUEST(HttpStatus.BAD_REQUEST, "PAPER_400_5", "차용증 데이터가 올바르지 않습니다."),

    PAPER_NOT_FOUND(HttpStatus.NOT_FOUND, "PAPER_404_1", "차용증 정보가 존재하지 않습니다."),

    PAPER_IS_NOT_MINE(HttpStatus.FORBIDDEN, "PAPER_403_1", "차용증 접근 권한이 없습니다."),
    PAPER_WRITER_CAN_MODIFY(HttpStatus.FORBIDDEN, "PAPER_403_2", "첫 작성자만 수정이 가능합니다."),

    //Repayment Error
    REPAYMENT_STATUS_ERROR(HttpStatus.BAD_REQUEST, "REPAYMENT_400_1", "상환은 차용증 작성이 완료되었을 때만 가능합니다."),
    REPAYMENT_NOT_VALID_DATE(HttpStatus.BAD_REQUEST, "REPAYMENT_400_2", "일부 상환 일자가 차용증과 일치하지 않습니다."),
    REPAYMENT_AMOUNT_OVER(HttpStatus.BAD_REQUEST, "REPAYMENT_400_3", "상환 기록액이 남은 금액을 초과합니다."),

    REPAYMENT_ONLY_ACCESS_CREDITOR(HttpStatus.FORBIDDEN, "REPAYMENT_403_1", "일부 상환은 채권자만 기록할 수 있습니다."),

    REPAYMENT_HISTORY_NOT_FOUND(HttpStatus.NOT_FOUND, "REPAYMENT_404_1", "상환 기록이 존재하지 않습니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String errorMessage;
}
