package com.owl.payrit.domain.repaymenthistory.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.owl.payrit.domain.promissorypaper.entity.PromissoryPaper;
import com.owl.payrit.global.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
public class RepaymentHistory extends BaseEntity {

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    private PromissoryPaper paper;

    private LocalDate repaymentDate;

    @Builder.Default
    private Long repaymentAmount = 0L;
}
