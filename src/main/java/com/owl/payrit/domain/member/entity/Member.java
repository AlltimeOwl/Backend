package com.owl.payrit.domain.member.entity;

import com.owl.payrit.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Entity
public class Member extends BaseEntity {

    @Embedded
    private OauthInformation oauthInformation;

    @Column
    private String email;

    private String phoneNumber;

    private LocalDate birthDay;

    private boolean isAuthenticated;

    private boolean isAgreeNotification;

    private String firebaseToken;

    private String address;

    private Role role;

}
