package com.owl.payrit.domain.promissorypaper.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class PaperAmount {

    private long primeAmount;           //순수 원금
    private long interest;              //이자
    private long amount;                //순수 원금 + 이자 (최종 금액)
    private long remainingAmount;       //남은 금액

    public void repayment(long repaymentAmount) {
        this.remainingAmount = remainingAmount - repaymentAmount;
    }
}
