package com.owl.payrit.domain.repaymenthistory.dto;

import java.time.LocalDate;

public record RepaymentHistoryDto(
        Long id,
        LocalDate repaymentDate,
        long repaymentAmount
) {
}
