package com.owl.payrit.domain.member.entity;

import com.owl.payrit.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Entity
@Table(name = "member",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "OAUTH",
            columnNames = {
                "oauth_provider_id",
                "oauth_provider"
            }
        )
//        @UniqueConstraint(
//            name = "DUPLICATED",
//            columnNames = {
//                "name",
//                "phone_number"
//            }
//        )
    })
public class Member extends BaseEntity implements UserDetails {

    @Embedded
    private OauthInformation oauthInformation;

    @Embedded
    private CertificationInformation certificationInformation;

    @Column(nullable = false)
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column
    private LocalDate birthDay;

    @Column
    private String name;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isAuthenticated;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isAgreeNotification;

    @Column
    private String firebaseToken;

    @Column
    private String address;

    @Column(nullable = false)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(getRole().name()));
    }

    public void modifyAlarmStatus() {
        this.isAgreeNotification = !this.isAgreeNotification;
    }

    public void upsertFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

    public void updateCertificationInformation(CertificationInformation certificationInformation) {
        this.certificationInformation = certificationInformation;
        this.isAuthenticated = true;
    }
    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
