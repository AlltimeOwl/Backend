package com.owl.payrit.domain.auth.provider.apple;

import com.owl.payrit.domain.auth.exception.AuthErrorCode;
import com.owl.payrit.domain.auth.exception.AuthException;

import java.util.List;

public record ApplePublicKeyResponse(
    List<ApplePublicKey> publicKeys
) {

    public ApplePublicKey getProperKey(String kid, String alg) {
        return publicKeys.stream()
            .filter(key -> key.kid().equals(kid) && key.alg().equals(alg))
            .findFirst()
            .orElseThrow(() -> new AuthException(AuthErrorCode.IMPROPER_OAUTH_INFORMATION));
    }
}
