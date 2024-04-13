package com.owl.payrit.domain.auth.provider.apple;

import com.owl.payrit.domain.auth.domain.OauthProvider;
import com.owl.payrit.domain.auth.dto.request.AppleRevokeRequest;
import com.owl.payrit.domain.auth.dto.request.AppleTokenGenerateRequest;
import com.owl.payrit.domain.auth.dto.request.LoginTokenRequest;
import com.owl.payrit.domain.auth.dto.response.AppleTokenResponse;
import com.owl.payrit.domain.auth.dto.response.AppleUser;
import com.owl.payrit.domain.auth.exception.AuthErrorCode;
import com.owl.payrit.domain.auth.exception.AuthException;
import com.owl.payrit.domain.auth.provider.OauthClient;
import com.owl.payrit.domain.member.entity.Member;
import io.jsonwebtoken.Claims;
import java.security.PublicKey;
import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
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
    public Member fetch(LoginTokenRequest loginTokenRequest) {
        ApplePublicKeyResponse applePublicKeyResponse = appleApiClient.fetchPublicKey();
        Map<String, String> appleToken = appleJwtValidator.parseHeaders(loginTokenRequest.accessToken());
        PublicKey publicKey = applePublicKeyGenerator.generate(appleToken, applePublicKeyResponse);
        Claims claims = appleJwtValidator.getTokenClaims(loginTokenRequest.accessToken(), publicKey);
        return mapClaimToMember(claims);
    }

    @Override
    public void revoke(String authorizationCode) {
        AppleTokenGenerateRequest appleTokenGenerateRequest = appleJwtValidator.generateAppleToken(authorizationCode);
        log.info("TokenGenerate Result is : {}", appleTokenGenerateRequest.toString());
        AppleTokenResponse appleTokenResponse = generateAppleToken(appleTokenGenerateRequest.toRequestBody());
        log.info("TokenGenerate Response is : {}", appleTokenResponse.toString());
        AppleRevokeRequest appleRevokeRequest = appleJwtValidator.generateAppleRevokeRequest(appleTokenResponse.accessToken());
        log.info("RevokeRequest is : {}", appleRevokeRequest.toString());
        revokeAppleToken(appleRevokeRequest.toRequestBody());

    }

    @Override
    public String requestRefreshToken(String authorizationCode) {
        AppleTokenGenerateRequest appleTokenGenerateRequest = appleJwtValidator.generateAppleToken(authorizationCode);
        log.info("TokenGenerate Result is : {}", appleTokenGenerateRequest.toString());
        AppleTokenResponse appleTokenResponse = generateAppleToken(appleTokenGenerateRequest.toRequestBody());
        log.info("TokenGenerate Response is : {}", appleTokenResponse.toString());
        return appleTokenResponse.refreshToken();
    }

    public Member mapClaimToMember(Claims claims) {
        final String SUB = "sub";
        final String EMAIL = "email";

        final String appleId = claims.get(SUB, String.class);
        final String appleEmail = claims.get(EMAIL, String.class);

        AppleUser appleUser = new AppleUser(appleId, appleEmail);
        return appleUser.toEntity();
    }

    public AppleTokenResponse generateAppleToken(MultiValueMap<String, String> requestBody) {
        log.info("generateAppleToken");
        RestTemplate restTemplate = new RestTemplateBuilder().build();
        String authUrl = "https://appleid.apple.com/auth/token";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(requestBody, headers);
        try {
            ResponseEntity<AppleTokenResponse> response = restTemplate.postForEntity(authUrl, httpEntity, AppleTokenResponse.class);
            log.info("Response Status: {}" , response.getStatusCode());
            log.info("Response Body: {}" , response.getBody());
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw new AuthException(AuthErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public void revokeAppleToken(MultiValueMap<String, String> requestBody) {
        log.info("generate Revoke Token");
        if (requestBody.get("token") != null) {
            RestTemplate restTemplate = new RestTemplateBuilder().build();
            String revokeUrl = "https://appleid.apple.com/auth/revoke";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> revokeResponse = restTemplate.postForEntity(revokeUrl, httpEntity, String.class);
            log.info("Response Status: {}" , revokeResponse.getStatusCode());
            log.info("Response Body: {}" , revokeResponse.getBody());
        }
    }


}
