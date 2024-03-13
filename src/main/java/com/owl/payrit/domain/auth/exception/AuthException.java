package com.owl.payrit.domain.auth.exception;

import com.owl.payrit.global.exception.BaseException;
import com.owl.payrit.global.exception.ErrorCode;

public class AuthException extends BaseException {

    public AuthException(ErrorCode errorCode) {
        super(errorCode);
    }
}
