package com.owl.payrit.domain.auth.api;

import com.owl.payrit.domain.auth.dto.response.KakaoOauthResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;

public interface KakaoApiClient {

    @GetExchange("https://kapi.kakao.com/v2/user/me")
    KakaoOauthResponse fetchMember(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String bearerToken);

}
