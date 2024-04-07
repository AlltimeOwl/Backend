package com.owl.payrit.domain.auth.provider.danal;

import com.owl.payrit.domain.auth.dto.request.PortOneTokenRequest;
import com.owl.payrit.domain.auth.dto.response.PortOneTokenResponse;
import org.springframework.web.service.annotation.PostExchange;

public interface PortOneApiClient {

    @PostExchange("https://api.iamport.kr/users/getToken")
    PortOneTokenResponse getAccessToken(PortOneTokenRequest portOneTokenRequest);
}
