package com.owl.payrit.domain.memo.entity;

import com.owl.payrit.domain.promissorypaper.entity.PromissoryPaper;
import com.owl.payrit.global.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Memo extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promissory_paper_id")
    private PromissoryPaper promissoryPaper;

    private Long memberId;

    private String content;

    public void update(String content) {
        this.content = content;
    }
}
