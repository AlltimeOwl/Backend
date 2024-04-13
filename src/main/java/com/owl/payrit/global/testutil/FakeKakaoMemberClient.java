package com.owl.payrit.global.testutil;

import com.owl.payrit.domain.auth.domain.OauthProvider;
import com.owl.payrit.domain.auth.dto.request.LoginTokenRequest;
import com.owl.payrit.domain.auth.provider.OauthClient;
import com.owl.payrit.domain.member.entity.Member;
import com.owl.payrit.domain.member.entity.OauthInformation;
import com.owl.payrit.domain.member.entity.Role;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class FakeKakaoMemberClient implements OauthClient {

    @Override
    public OauthProvider oauthProvider() {
        return OauthProvider.FAKE_KAKAO;
    }

    @Override
    public Member fetch(LoginTokenRequest loginTokenRequest) {
        // 가짜 유저 정보를 생성하여 반환
        OauthInformation fakeOauthInformation = new OauthInformation("fake_oauth_provider_id", OauthProvider.FAKE_KAKAO, null);
        return new Member(fakeOauthInformation, null ,"fake_email@example.com", "fake_phone_number", LocalDate.now(),"test",true, true, "fake_firebase_token", "fake_address", Role.MEMBER);
    }

    @Override
    public void revoke(String authorizationCode) {

    }
}
