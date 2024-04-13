package com.owl.payrit.domain.member.entity;

import com.owl.payrit.domain.auth.domain.OauthProvider;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Embeddable
public class OauthInformation {

    @Column(nullable = false, name = "oauth_provider_id")
    private String oauthProviderId;

    @Column(nullable = false, name = "oauth_provider")
    @Enumerated(EnumType.STRING)
    private OauthProvider oauthProvider;

    @Column(nullable = true, name = "apple_refresh_token")
    private String appleRefreshToken;

    public void updateAppleRefreshToken(String refreshToken) {
        this.appleRefreshToken = refreshToken;
    }
}
