package com.owl.payrit.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    EXAMPLE_EXCEPTION(HttpStatus.BAD_REQUEST, "예시 에러입니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원이 존재하지 않습니다."),
    PAPER_NOT_FOUND(HttpStatus.NOT_FOUND, "차용증 정보가 존재하지 않습니다."),
    PAPER_IS_NOT_MINE(HttpStatus.FORBIDDEN, "차용증 접근 권한이 없습니다."),
    PAPER_LIST_EXCEPTION(HttpStatus.BAD_REQUEST, "차용증 리스트 조회가 올바르지 않습니다."),
    PAPER_CANNOT_ACCEPT_SELF(HttpStatus.BAD_REQUEST, "작성자는 승인할 수 없습니다."),
    PAPER_STATUS_NOT_VALID(HttpStatus.BAD_REQUEST, "차용증에 대한 요청 단계가 올바르지 않습니다."),
    PAPER_WRITER_CAN_MODIFY(HttpStatus.FORBIDDEN, "첫 작성자만 수정이 가능합니다."),
    PAPER_MATCHING_FAILED(HttpStatus.BAD_REQUEST, "차용증 정보와 회원의 정보가 일치하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String errorMessage;
}
