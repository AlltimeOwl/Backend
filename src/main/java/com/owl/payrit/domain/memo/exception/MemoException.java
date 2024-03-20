package com.owl.payrit.domain.memo.exception;

import com.owl.payrit.global.exception.PayritCodeException;

public class MemoException extends PayritCodeException {

    public MemoException(MemoErrorCode errorCode) {
        super(errorCode);
    }
}
