package com.owl.payrit.domain.promissorypaper.exception;

import com.owl.payrit.global.exception.BaseException;
import com.owl.payrit.global.exception.ErrorCode;

public class PromissoryPaperException extends BaseException {

    public PromissoryPaperException(ErrorCode errorCode) {
        super(errorCode);
    }
}
