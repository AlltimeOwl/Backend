package com.owl.payrit.domain.member.entity;

import com.owl.payrit.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class Member extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private OauthProvider oauthProvider;

    @Column
    private String email;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate birthDay;

    private boolean isAuthenticated;

    private boolean isAgreeNotification;

    private String firebaseToken;

    private String address;


}
