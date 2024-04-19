package com.owl.payrit.domain.promise.exception;

import com.owl.payrit.global.exception.PayritCodeException;

public class PromiseException extends PayritCodeException {
    public PromiseException(PromiseErrorCode errorCode) {
        super(errorCode);
    }
}
