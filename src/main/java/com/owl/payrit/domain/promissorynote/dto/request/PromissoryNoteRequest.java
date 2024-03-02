package com.owl.payrit.domain.promissorynote.dto.request;

import java.time.LocalDate;

public record PromissoryNoteRequest(
        long amount,                        //총액
        LocalDate transactionDate,          //거래 날짜
        LocalDate repaymentStartDate,       //상환 시작일
        LocalDate repaymentEndDate,         //상환 종료일
        String specialConditions,           //특약 사항
        String creditorName,                //채권자 이름
        String creditorPhoneNumber,         //채권자 휴대폰 번호
        String creditorAddress,             //채권자 주소
        String debtorName,                  //채무자 이름
        String debtorPhoneNumber,           //채무자 휴대폰 번호
        String debtorAddress                //채무자 주소
) {
}
