package com.owl.payrit.domain.promissorypaper.dto.request;

import com.owl.payrit.domain.promissorypaper.entity.PaperRole;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record PaperWriteRequest(

        @Schema(description = "작성자 역할 (CREDITOR or DEBTOR)")
        PaperRole writerRole,                //작성자 역할(CREDITOR or DEBTOR)
        long amount,
        LocalDate transactionDate,          //거래 날짜
        LocalDate repaymentStartDate,       //상환 시작일
        LocalDate repaymentEndDate,         //상환 종료일
        String specialConditions,           //특약 사항
        float interestRate,                 //이자율
        String creditorName,
        String creditorPhoneNumber,
        String creditorAddress,
        String debtorName,
        String debtorPhoneNumber,
        String debtorAddress
) {
}
