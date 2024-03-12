package com.owl.payrit.domain.auth.provider.apple;

import com.owl.payrit.domain.auth.exception.AuthException;
import com.owl.payrit.global.exception.ErrorCode;
import java.util.List;

public record ApplePublicKeyResponse(
    List<ApplePublicKey> publicKeys
) {

    public ApplePublicKey getProperKey(String kid, String alg) {
        return publicKeys.stream()
            .filter(key -> key.kid().equals(kid) && key.alg().equals(alg))
            .findFirst()
            .orElseThrow(() -> new AuthException(ErrorCode.IMPROPER_OAUTH_INFORMATION));
    }
}
