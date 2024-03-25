package com.owl.payrit.domain.docsinfo.entity;

import com.owl.payrit.domain.member.entity.Member;
import com.owl.payrit.domain.promissorypaper.entity.PromissoryPaper;
import com.owl.payrit.global.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
public class DocsInfo extends BaseEntity {

    @OneToOne
    private PromissoryPaper linkedPaper;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member writer;

    private String writerIpAddr;

    private String writerCI;                 //TODO: Memmber Entity CI 추가

    @ManyToOne(fetch = FetchType.LAZY)
    private Member accepter;

    private String accepterIpAddr;

    private String accepterCI;

    private LocalDate acceptDate;

    private String docsKey;

    private String docsUrl;
}