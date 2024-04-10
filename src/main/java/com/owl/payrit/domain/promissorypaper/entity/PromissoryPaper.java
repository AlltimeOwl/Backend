package com.owl.payrit.domain.promissorypaper.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.owl.payrit.domain.docsinfo.entity.DocsInfo;
import com.owl.payrit.domain.member.entity.Member;
import com.owl.payrit.domain.memo.entity.Memo;
import com.owl.payrit.domain.repaymenthistory.entity.RepaymentHistory;
import com.owl.payrit.global.encryption.PromissoryPaperStringConverter;
import com.owl.payrit.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@DynamicUpdate
public class PromissoryPaper extends BaseEntity {

    @Embedded
    private PaperFormInfo paperFormInfo;

    @JsonManagedReference
    @OneToMany(mappedBy = "paper", fetch = FetchType.LAZY)
    private List<RepaymentHistory> repaymentHistory;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member writer;

    @Enumerated(EnumType.STRING)
    private PaperRole writerRole;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member creditor;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "creditor_name")),
            @AttributeOverride(name = "phoneNumber", column = @Column(name = "creditor_phone_number")),
            @AttributeOverride(name = "address", column = @Column(name = "creditor_address")),
    })
    private PaperProfile creditorProfile;

    private boolean isCreditorAgree;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member debtor;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "debtor_name")),
            @AttributeOverride(name = "phoneNumber", column = @Column(name = "debtor_phone_number")),
            @AttributeOverride(name = "address", column = @Column(name = "debtor_address")),
    })
    private PaperProfile debtorProfile;

    private boolean isDebtorAgree;

    @Builder.Default
    private boolean isPaid = false;

    @JoinColumn(name = "DOCS_INFO_ID")
    @OneToOne(cascade = CascadeType.ALL)
    private DocsInfo docsInfo;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private PaperStatus paperStatus = PaperStatus.WAITING_AGREE;

    @OneToMany(mappedBy = "promissoryPaper", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Memo> memos = new ArrayList<>();

    public void modifyPaperStatus(PaperStatus status) {
        this.paperStatus = status;
    }

    public void paid() {
        this.isPaid = true;
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


