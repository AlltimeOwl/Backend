package com.owl.payrit.domain.docsinfo.exception;

import com.owl.payrit.global.exception.PayritCodeException;

public class DocsInfoException extends PayritCodeException {

    public DocsInfoException(DocsInfoErrorCode errorCode) {
        super(errorCode);
    }
}
