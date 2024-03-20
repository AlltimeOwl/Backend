package com.owl.payrit.domain.auth.exception;

import com.owl.payrit.global.exception.PayritCodeException;

public class AuthException extends PayritCodeException {

    public AuthException(AuthErrorCode errorCode) {
        super(errorCode);
    }
}
