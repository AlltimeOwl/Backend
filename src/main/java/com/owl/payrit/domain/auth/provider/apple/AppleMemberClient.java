package com.owl.payrit.domain.auth.provider.apple;

import com.owl.payrit.domain.auth.domain.OauthProvider;
import com.owl.payrit.domain.auth.provider.OauthClient;
import com.owl.payrit.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AppleMemberClient implements OauthClient {

    private final AppleApiClient appleApiClient;

    @Override
    public OauthProvider oauthProvider() {
        return OauthProvider.APPLE;
    }

    @Override
    public Member fetch(String accessToken) {
        return null;
    }
}
