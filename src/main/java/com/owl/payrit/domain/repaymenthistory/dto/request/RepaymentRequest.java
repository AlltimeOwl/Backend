package com.owl.payrit.domain.repaymenthistory.dto.request;

import java.time.LocalDate;

public record RepaymentRequest(

        long paperId,
        LocalDate repaymentDate,
        long repaymentAmount
) {
}
