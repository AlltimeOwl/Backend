package com.owl.payrit.domain.docsinfo.entity;

import com.owl.payrit.domain.member.entity.Member;
import com.owl.payrit.global.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
public class DocsInfo extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Member writer;

    private String writerIpAddr;

    private String writerCI;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member accepter;

    private String accepterIpAddr;

    private String accepterCI;

    private LocalDate acceptDate;

    private String docsKey;

    private String docsUrl;
}