package com.owl.payrit.domain.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class Member {

    @Id
    private Long id;

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
