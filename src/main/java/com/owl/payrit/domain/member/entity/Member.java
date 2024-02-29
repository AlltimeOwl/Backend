package com.owl.payrit.domain.member.entity;

import com.owl.payrit.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Table(name = "member",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "oauth_unique",
            columnNames = {
                "oauth_provider_id",
                "oauth_provider"
            }
        )
    })
public class Member extends BaseEntity {

    @Embedded
    private OauthInformation oauthInformation;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private LocalDate birthDay;

    @Column
    private boolean isAuthenticated;

    @Column
    private boolean isAgreeNotification;

    @Column
    private String firebaseToken;

    @Column
    private String address;

    @Column(nullable = false)
    private Role role;

}
