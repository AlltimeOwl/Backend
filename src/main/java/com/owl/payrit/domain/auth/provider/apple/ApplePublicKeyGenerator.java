package com.owl.payrit.domain.auth.provider.apple;

import com.owl.payrit.domain.auth.exception.AuthException;
import com.owl.payrit.global.exception.ErrorCode;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ApplePublicKeyGenerator {

    public PublicKey generate(Map<String, String> headers, ApplePublicKeyResponse applePublicKeyResponse) {
        ApplePublicKey applePublicKey = applePublicKeyResponse.getProperKey(headers.get("kid"), headers.get("alg"));
        return getGenerateKey(applePublicKey);
    }

    private PublicKey getGenerateKey(ApplePublicKey applePublicKey) {
        byte[] nBytes = Base64.getUrlDecoder().decode(applePublicKey.n());
        byte[] eBytes = Base64.getUrlDecoder().decode(applePublicKey.e());

        try {
            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(new BigInteger(1, nBytes), new BigInteger(1, eBytes));
            KeyFactory keyFactory = KeyFactory.getInstance(applePublicKey.kty());
            return keyFactory.generatePublic(publicKeySpec);
        } catch(NoSuchAlgorithmException | InvalidKeySpecException exception) {
            throw new AuthException(ErrorCode.IMPROPER_OAUTH_INFORMATION);
        }
    }
}
