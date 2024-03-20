package com.owl.payrit.domain.repaymenthistory.exception;

import com.owl.payrit.global.exception.PayritCodeException;

public class RepaymentException extends PayritCodeException {

    public RepaymentException(RepaymentErrorCode errorCode) {
        super(errorCode);
    }
}
