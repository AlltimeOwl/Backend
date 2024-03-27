package com.owl.payrit.domain.promissorypaper.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.owl.payrit.domain.member.entity.Member;
import com.owl.payrit.domain.memo.entity.Memo;
import com.owl.payrit.domain.repaymenthistory.entity.RepaymentHistory;
import com.owl.payrit.global.encryption.PromissoryPaperStringConverter;
import com.owl.payrit.global.entity.BaseEntity;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@DynamicUpdate
public class PromissoryPaper extends BaseEntity {

    private long primeAmount;           //순수 원금

    private long interest;              //이자

    private long amount;                //순수 원금 + 이자 (최종 금액)

    private long remainingAmount;       //남은 금액

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

    @Convert(converter = PromissoryPaperStringConverter.class)
    private String creditorName;

    @Convert(converter = PromissoryPaperStringConverter.class)
    private String creditorPhoneNumber;

    @Convert(converter = PromissoryPaperStringConverter.class)
    private String creditorAddress;

    private boolean isCreditorAgree;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member debtor;

    @Convert(converter = PromissoryPaperStringConverter.class)
    private String debtorName;

    @Convert(converter = PromissoryPaperStringConverter.class)
    private String debtorPhoneNumber;

    @Convert(converter = PromissoryPaperStringConverter.class)
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

    @OneToMany(mappedBy = "promissoryPaper", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Memo> memos = new ArrayList<>();

    public void modifyPaperStatus(PaperStatus status) {
        this.paperStatus = status;
    }

    public void removeCreditorRelation() {
        this.creditor = null;
    }

    public void removeDebtorRelation() {
        this.debtor = null;
    }

    public void removeWriterRelation() {
        this.writer = null;
    }
}


