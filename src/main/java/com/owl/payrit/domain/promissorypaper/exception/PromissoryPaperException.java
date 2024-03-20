package com.owl.payrit.domain.promissorypaper.exception;

import com.owl.payrit.global.exception.PayritCodeException;

public class PromissoryPaperException extends PayritCodeException {
    public PromissoryPaperException(PromissoryPaperErrorCode errorCode) {
        super(errorCode);
    }
}
