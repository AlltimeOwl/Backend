package com.owl.payrit.domain.auth.provider.kakao;

import com.owl.payrit.domain.auth.domain.OauthProvider;
import com.owl.payrit.domain.auth.dto.response.KakaoOauthResponse;
import com.owl.payrit.domain.auth.provider.OauthClient;
import com.owl.payrit.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class KakaoMemberClient implements OauthClient {

    private final KakaoApiClient kakaoApiClient;

    @Override
    public OauthProvider oauthProvider() {
        return OauthProvider.KAKAO;
    }

    @Override
    public Member fetch(String accessToken) {
        KakaoOauthResponse kakaoOauthResponse = kakaoApiClient.fetchMember("Bearer " + accessToken);
        return kakaoOauthResponse.toEntity();
    }
}
