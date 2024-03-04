package com.owl.payrit.domain.auth.domain;

import java.util.Locale;

public enum OauthProvider {
    KAKAO, APPLE;

    public static OauthProvider getName(String provider) {
        return OauthProvider.valueOf(provider.toUpperCase(Locale.ENGLISH));
    }
}
