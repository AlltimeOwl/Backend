package com.owl.payrit.domain.auth.provider.kakao;

import com.owl.payrit.domain.auth.domain.OauthProvider;
import com.owl.payrit.domain.auth.dto.request.LoginTokenRequest;
import com.owl.payrit.domain.auth.dto.response.KakaoOauthResponse;
import com.owl.payrit.domain.auth.provider.OauthClient;
import com.owl.payrit.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class KakaoMemberClient implements OauthClient {

    private final KakaoApiClient kakaoApiClient;

    @Override
    public OauthProvider oauthProvider() {
        return OauthProvider.KAKAO;
    }

    @Override
    public Member fetch(LoginTokenRequest loginTokenRequest) {
        KakaoOauthResponse kakaoOauthResponse = kakaoApiClient.fetchMember("Bearer " + loginTokenRequest.accessToken());
        return kakaoOauthResponse.toEntity();
    }

    @Override
    public void revoke(String authorizationCode) {
      log.info("kakao user requests revoke");
    }

    @Override
    public String requestRefreshToken(String authorizationCode) {
        // 카카오는 필요 없지만, 정보 갱신을 위해 추후 구현 예정
        return null;
    }
}
