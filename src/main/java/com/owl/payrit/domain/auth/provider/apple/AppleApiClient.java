package com.owl.payrit.domain.auth.provider.apple;

import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

public interface AppleApiClient {

    @GetExchange("https://appleid.apple.com/auth/keys")
    ApplePublicKeyResponse fetchPublicKey();

    @PostExchange("https://appleid.apple.com/auth/token")
    void generateAppleToken();

    @PostExchange("https://appleid.apple.com/auth/revoke")
    void revokeAppleToken();

}
