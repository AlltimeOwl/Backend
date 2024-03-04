package com.owl.payrit.domain.promissorypaper.entity;

import com.owl.payrit.domain.member.entity.Member;
import com.owl.payrit.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
public class PromissoryPaper extends BaseEntity {

    private long amount;

    private LocalDate transactionDate;

    private LocalDate repaymentStartDate;

    private LocalDate repaymentEndDate;

    private String specialConditions;

    private int interestRate;

    @ColumnDefault("0")
    private long currentRepaymentAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member creditor;

    private String creditorPhoneNumber;

    private String creditorAddress;

    private boolean isCreditorAgree;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member debtor;

    private String debtorPhoneNumber;

    private String debtorAddress;

    private boolean isDebtorAgree;
    
    private boolean isPaid;

    private String noteKey;    //차용증 고유 값

    //차용증을 누가 작성했는지
    @ManyToOne(fetch = FetchType.LAZY)
    private Member writer;
    
    //차용증 상태
    @Enumerated(EnumType.STRING)
    private PaperStatus paperStatus;

    //저장소 URL
    private String storageUrl;
}
