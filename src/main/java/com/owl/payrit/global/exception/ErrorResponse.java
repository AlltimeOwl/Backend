package com.owl.payrit.global.exception;

import org.springframework.http.HttpStatus;

public record ErrorResponse(
    HttpStatus httpStatus,
    String code,
    String message
) {
    public static ErrorResponse from(BaseException baseException) {
        return new ErrorResponse(baseException.errorCode.getHttpStatus(), baseException.errorCode.getCode(), baseException.errorCode.getErrorMessage());
    }
}
