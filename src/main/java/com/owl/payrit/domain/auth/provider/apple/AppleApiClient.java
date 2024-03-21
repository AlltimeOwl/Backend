package com.owl.payrit.domain.auth.provider.apple;

import com.owl.payrit.domain.auth.dto.request.AppleTokenGenerateRequest;
import com.owl.payrit.domain.auth.dto.response.AppleTokenResponse;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

public interface AppleApiClient {

    @GetExchange("https://appleid.apple.com/auth/keys")
    ApplePublicKeyResponse fetchPublicKey();

    @PostExchange("https://appleid.apple.com/auth/token")
    AppleTokenResponse generateAppleToken(AppleTokenGenerateRequest appleTokenGenerateRequest);

    @PostExchange("https://appleid.apple.com/auth/revoke")
    void revokeAppleToken();

}
