package com.owl.payrit.domain.member.exception;

import com.owl.payrit.global.exception.BaseException;
import com.owl.payrit.global.exception.ErrorCode;

public class MemberException extends BaseException {

    public MemberException(ErrorCode errorCode) {
        super(errorCode);
    }
}
