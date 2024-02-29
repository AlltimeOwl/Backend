package com.owl.payrit.domain.member.entity;

import com.owl.payrit.domain.auth.domain.OauthProvider;
import com.owl.payrit.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Entity
public class Member extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private OauthProvider oauthProvider;

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
