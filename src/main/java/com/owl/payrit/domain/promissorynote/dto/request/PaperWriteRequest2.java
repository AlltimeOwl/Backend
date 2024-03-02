package com.owl.payrit.domain.promissorynote.dto.request;

import java.time.LocalDate;

public record PaperWriteRequest2(

        boolean isCreditor,                 //회원 역할
        long amount,                        //총액
        LocalDate transactionDate,          //거래 날짜
        LocalDate repaymentStartDate,       //상환 시작일
        LocalDate repaymentEndDate,         //상환 종료일
        String specialConditions,           //특약 사항
        String myName,                //채권자 이름(혹은 Member)
        String myPhoneNumber,         //채권자 휴대폰 번호
        String myAddress,             //채권자 주소
        String yourName,                  //채무자 이름(혹은 Member)
        String yourPhoneNumber,           //채무자 휴대폰 번호
        String yourAddress                //채무자 주소
) {
}
