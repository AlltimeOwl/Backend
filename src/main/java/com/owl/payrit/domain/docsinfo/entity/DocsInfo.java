package com.owl.payrit.domain.docsinfo.entity;

import com.owl.payrit.global.encryption.PromissoryPaperStringConverter;
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

    @Convert(converter = PromissoryPaperStringConverter.class)
    private String writerName;

    @Convert(converter = PromissoryPaperStringConverter.class)
    private String writerPhoneNum;

    @Convert(converter = PromissoryPaperStringConverter.class)
    private String writerIpAddr;

    private String writerCI;

    private LocalDateTime createdAt;

    private Long accepterId;

    @Convert(converter = PromissoryPaperStringConverter.class)
    private String accepterName;

    @Convert(converter = PromissoryPaperStringConverter.class)
    private String accepterPhoneNum;

    @Convert(converter = PromissoryPaperStringConverter.class)
    private String accepterIpAddr;

    private String accepterCI;

    private LocalDateTime acceptedAt;

    private String docsKey;

    private String docsUrl;
}