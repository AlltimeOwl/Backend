package com.owl.payrit.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    EXAMPLE_EXCEPTION(HttpStatus.BAD_REQUEST, "예시 에러입니다.");

    private final HttpStatus httpStatus;
    private final String errorMessage;
}
