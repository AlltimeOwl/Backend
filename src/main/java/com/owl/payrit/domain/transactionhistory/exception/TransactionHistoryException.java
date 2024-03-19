package com.owl.payrit.domain.transactionhistory.exception;

import com.owl.payrit.global.exception.PayritCodeException;

public class TransactionHistoryException extends PayritCodeException {

    public TransactionHistoryException(TransactionHistoryErrorCode errorCode) {
        super(errorCode);
    }
}
