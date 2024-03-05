package com.owl.payrit.domain.promissorypaper.dto.response;

import java.time.LocalDate;

public record DetailResponse(
    //FIXME: 단순 Paper객체가 아닌 Response를 반환하는 것과의 차이점...?

    String paperUrl,
    long totalAmount,                   //원금 + 이자로 이루어진 총액 (차용 금액 X)
    int repaymentRate,                  //상환 비율({현재 상환액} / {원금 + 이자})
    int currentRepaymentAmount,
    LocalDate repaymentStartDate,       //FIXME: 거래 날짜 : repaymentStartDate or transactionDate
    LocalDate repaymentEndDate,
    String creditorName,
    String creditorPhoneNumber,
    String creditorAddress,
    String debtorName,
    String debtorPhoneNumber,
    String debtorAddress,
    String specialConditions
) {
}
