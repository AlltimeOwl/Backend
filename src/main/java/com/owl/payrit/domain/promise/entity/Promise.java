package com.owl.payrit.domain.promise.entity;

import com.owl.payrit.domain.member.entity.Member;
import com.owl.payrit.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@DynamicUpdate
public class Promise extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Member writer;

    private long amount;

    private LocalDate promiseStartDate;

    private LocalDate promiseEndDate;

    private String contents;

    @ElementCollection
    private List<ParticipantsInfo> participants;
}
