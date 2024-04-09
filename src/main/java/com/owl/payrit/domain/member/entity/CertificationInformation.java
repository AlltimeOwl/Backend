package com.owl.payrit.domain.member.entity;

import com.owl.payrit.global.encryption.PromissoryPaperStringConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Embeddable
public class CertificationInformation {

    @Column(name = "imp_uid")
    private String impUid;

    @Column(name = "certification_name")
    @Convert(converter = PromissoryPaperStringConverter.class)
    private String name;

    @Column(name = "certification_gender")
    @Convert(converter = PromissoryPaperStringConverter.class)
    private String gender;

    @Column(name = "certification_birthday")
    @Convert(converter = PromissoryPaperStringConverter.class)
    private String birthday;

    @Column(name = "certification_phone_number")
    @Convert(converter = PromissoryPaperStringConverter.class)
    private String phone;

}
