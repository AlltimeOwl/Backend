package com.owl.payrit.domain.auth.provider.apple;

import com.owl.payrit.domain.auth.domain.OauthProvider;
import com.owl.payrit.domain.auth.dto.response.AppleUser;
import com.owl.payrit.domain.auth.provider.OauthClient;
import com.owl.payrit.domain.member.entity.Member;
import io.jsonwebtoken.Claims;
import java.security.PublicKey;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AppleMemberClient implements OauthClient {

    private final AppleApiClient appleApiClient;
    private final ApplePublicKeyGenerator applePublicKeyGenerator;
    private final AppleJwtValidator appleJwtValidator;

    @Override
    public OauthProvider oauthProvider() {
        return OauthProvider.APPLE;
    }

    @Override
    public Member fetch(String accessToken) {
        ApplePublicKeyResponse applePublicKeyResponse = appleApiClient.fetchPublicKey();
        Map<String, String> appleToken = appleJwtValidator.parseHeaders(accessToken);
        PublicKey publicKey = applePublicKeyGenerator.generate(appleToken, applePublicKeyResponse);
        Claims claims = appleJwtValidator.getTokenClaims(accessToken, publicKey);
        return mapClaimToMember(claims);
    }

    public Member mapClaimToMember(Claims claims) {
        final String SUB = "sub";
        final String EMAIL = "email";

        final String appleId = claims.get(SUB, String.class);
        final String appleEmail = claims.get(EMAIL, String.class);

        AppleUser appleUser = new AppleUser(appleId, appleEmail);
        return appleUser.toEntity();
    }
}
