package com.owl.payrit.domain.promissorypaper.dto.request;

import com.owl.payrit.domain.promissorypaper.entity.PaperRole;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record PaperWriteRequest(

        @Schema(description = "작성자 역할")
        PaperRole writerRole,

        @Schema(description = "금전거래 액수", minimum = "0", maximum = "30000000")
        long amount,
        
        @Schema(description = "차용증 작성 일자")
        LocalDate transactionDate,

        @Schema(description = "상환 시작일(빌려주기로 한 날짜)")
        LocalDate repaymentStartDate,

        @Schema(description = "상환 마감일(갚기로 한 날짜)")
        LocalDate repaymentEndDate,

        @Schema(description = "특약 사항")
        String specialConditions,

        @Schema(description = "이자율(0~20)", minimum = "0", maximum = "20")
        float interestRate,

        @Schema(description = "이자 지급일 (~28)", maximum = "28")
        long interestPaymentDate,

        @Schema(description = "채권자 이름")
        String creditorName,

        @Schema(description = "채권자 전화번호", example = "010-1234-0000")
        String creditorPhoneNumber,

        @Schema(description = "채권자 주소", example = "(12345) 서울시 종로구 광화문로 1234")
        String creditorAddress,

        @Schema(description = "채무자 이름")
        String debtorName,

        @Schema(description = "채무자 전화번호", example = "010-1234-0000")
        String debtorPhoneNumber,

        @Schema(description = "채무자 주소", example = "(12345) 서울시 종로구 광화문로 1234")
        String debtorAddress
) {
}
