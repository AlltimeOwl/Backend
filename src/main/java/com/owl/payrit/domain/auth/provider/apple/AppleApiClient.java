package com.owl.payrit.domain.auth.provider.apple;

import com.owl.payrit.domain.auth.dto.request.AppleRevokeRequest;
import com.owl.payrit.domain.auth.dto.response.AppleTokenResponse;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

public interface AppleApiClient {

    @GetExchange("https://appleid.apple.com/auth/keys")
    ApplePublicKeyResponse fetchPublicKey();

    @PostExchange(value = "https://appleid.apple.com/auth/token", contentType = "application/x-www-form-urlencoded")
    AppleTokenResponse generateAppleToken(@RequestBody MultiValueMap<String, String> multiValueMap);

    @PostExchange("https://appleid.apple.com/auth/revoke")
    void revokeAppleToken(@RequestBody AppleRevokeRequest appleRevokeRequest);

}
