package com.owl.payrit.domain.repaymenthistory.dto.request;

public record RepaymentCancelRequest(
        Long paperId,
        Long historyId
) {
}
