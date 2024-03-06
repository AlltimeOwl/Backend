package com.owl.payrit.domain.auth.provider.apple;

import org.springframework.web.service.annotation.GetExchange;

public interface AppleApiClient {

    @GetExchange("https://appleid.apple.com/auth/keys")
    ApplePublicKeyResponse fetchPublicKey();

}
