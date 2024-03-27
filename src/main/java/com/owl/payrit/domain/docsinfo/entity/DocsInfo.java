package com.owl.payrit.domain.docsinfo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
public class DocsInfo {

    @Id
    @Column(name = "DOCS_INFO_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long writerId;

    private String writerName;

    private String writerPhoneNum;

    private String writerIpAddr;

    private String writerCI;

    private LocalDateTime createdAt;

    private Long accepterId;

    private String accepterName;

    private String accepterPhoneNum;

    private String accepterIpAddr;

    private String accepterCI;

    private LocalDateTime acceptedAt;

    private String docsKey;

    private String docsUrl;
}