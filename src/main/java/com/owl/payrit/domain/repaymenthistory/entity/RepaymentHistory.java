package com.owl.payrit.domain.repaymenthistory.entity;

import com.owl.payrit.domain.promissorypaper.entity.PromissoryPaper;
import com.owl.payrit.global.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
public class RepaymentHistory extends BaseEntity {

    @ManyToOne
    private PromissoryPaper paper;

    private LocalDate repaymentDate;

    private int repaymentRound;

    private long repaymentAmount;
}
