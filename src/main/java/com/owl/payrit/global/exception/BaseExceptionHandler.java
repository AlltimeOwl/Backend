package com.owl.payrit.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class BaseExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException baseException) {
        ErrorCode errorCode = baseException.getErrorCode();
        ErrorResponse errorResponse = ErrorResponse.from(baseException);

        log.error("Error occurred: {} - {}", errorResponse.httpStatus(), errorResponse.message(), baseException);
        return ResponseEntity.status(baseException.errorCode.getHttpStatus()).body(errorResponse);
    }
}
