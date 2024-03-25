package com.owl.payrit.domain.promissorypaper.entity;

import com.owl.payrit.domain.member.entity.Member;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class DocsInfo {

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
