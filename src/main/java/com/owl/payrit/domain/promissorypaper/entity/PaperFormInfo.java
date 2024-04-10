package com.owl.payrit.domain.promissorypaper.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class PaperFormInfo {

    private long primeAmount;           //순수 원금

    private long interest;              //이자

    private long amount;                //순수 원금 + 이자 (최종 금액)

    private long remainingAmount;       //남은 금액

    private LocalDate transactionDate;

    private LocalDate repaymentStartDate;

    private LocalDate repaymentEndDate;

    private String specialConditions;

    private float interestRate;

    private long interestPaymentDate;

    public PaperFormInfo repayment(long repaymentAmount) {

        this.remainingAmount = repaymentAmount;

        return this;
    }

    public PaperFormInfo cancelRepayment(long needToCancelAmount) {

        this.remainingAmount = remainingAmount + needToCancelAmount;

        return this;
    }

    public void addModifyMsg(String contents) {

        this.specialConditions = this.specialConditions.concat(contents);
    }
}
