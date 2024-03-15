package com.owl.payrit.domain.promissorypaper.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.owl.payrit.domain.member.entity.Member;
import com.owl.payrit.domain.repaymenthistory.entity.RepaymentHistory;
import com.owl.payrit.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@DynamicUpdate
public class PromissoryPaper extends BaseEntity {

    private long amount;

    private long remainingAmount;

    @JsonManagedReference
    @OneToMany(mappedBy = "paper", fetch = FetchType.LAZY)
    private List<RepaymentHistory> repaymentHistory;

    private LocalDate transactionDate;

    private LocalDate repaymentStartDate;

    private LocalDate repaymentEndDate;

    private String specialConditions;

    private float interestRate;

    private long interestPaymentDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member writer;

    @Enumerated(EnumType.STRING)
    private PaperRole writerRole;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member creditor;

    private String creditorName;

    private String creditorPhoneNumber;

    private String creditorAddress;

    private boolean isCreditorAgree;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member debtor;

    private String debtorName;

    private String debtorPhoneNumber;

    private String debtorAddress;

    private boolean isDebtorAgree;

    @Builder.Default
    private boolean isPaid = false;

    private String paperKey;    //차용증 고유 값
    
    //차용증 상태
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private PaperStatus paperStatus = PaperStatus.WAITING_AGREE;

    //저장소 URL
    private String storageUrl;

    public void modifyPaperStatus(PaperStatus status) {
        this.paperStatus = status;
    }
}
