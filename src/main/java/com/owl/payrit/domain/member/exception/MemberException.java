package com.owl.payrit.domain.member.exception;

import com.owl.payrit.global.exception.PayritCodeException;

public class MemberException extends PayritCodeException {

    public MemberException(MemberErrorCode errorCode) {
        super(errorCode);
    }

}
