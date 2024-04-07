package com.owl.payrit.domain.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Embeddable
public class CertificationInformation {

    @Column(name = "imp_uid")
    private String impUid;

    @Column(name = "certification_name")
    private String name;

    @Column(name = "certification_gender")
    private String gender;

    @Column(name = "certification_birthday")
    private String birthday;

    @Column(name = "certification_phone_number")
    private String phone;

}
