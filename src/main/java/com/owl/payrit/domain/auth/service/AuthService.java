package com.owl.payrit.domain.auth.service;

import com.owl.payrit.domain.auth.domain.OauthProvider;
import com.owl.payrit.domain.auth.dto.request.CertificationRequest;
import com.owl.payrit.domain.auth.dto.request.LoginTokenRequest;
import com.owl.payrit.domain.auth.dto.response.LoginUser;
import com.owl.payrit.domain.auth.dto.response.PortOneCertificationResponse;
import com.owl.payrit.domain.auth.dto.response.PortOneTokenResponse;
import com.owl.payrit.domain.auth.dto.response.TokenRefreshResponse;
import com.owl.payrit.domain.auth.dto.response.TokenResponse;
import com.owl.payrit.domain.auth.exception.AuthErrorCode;
import com.owl.payrit.domain.auth.exception.AuthException;
import com.owl.payrit.domain.auth.provider.OauthClientComposite;
import com.owl.payrit.domain.auth.provider.portone.PortOneApiClient;
import com.owl.payrit.domain.auth.util.JwtProvider;
import com.owl.payrit.domain.member.entity.CertificationInformation;
import com.owl.payrit.domain.member.entity.Member;
import com.owl.payrit.domain.member.entity.OauthInformation;
import com.owl.payrit.domain.member.service.MemberService;
import com.owl.payrit.domain.promissorypaper.service.PromissoryPaperService;
import com.owl.payrit.global.configuration.PortOneConfigProps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
@Service
public class AuthService {

    private final MemberService memberService;
    private final OauthClientComposite oauthClientComposite;
    private final PromissoryPaperService promissoryPaperService;
    private final JwtProvider jwtProvider;
    private final RedisTemplate<OauthInformation, String> oauthRedisTemplate;
    private final PortOneApiClient portOneApiClient;
    private final PortOneConfigProps portOneConfigProps;

    @Value("${jwt.token.secret}")
    private String secretKey;

    @Transactional
    public TokenResponse login(OauthProvider oauthProvider, LoginTokenRequest loginTokenRequest) {

        Member memberInformation = oauthClientComposite.fetch(oauthProvider, loginTokenRequest);
        Member savedMember = memberService.findByOauthInformationOrSave(memberInformation);

        if(savedMember.getOauthInformation().getAppleRefreshToken() == null) {
            String appleRefreshToken = requestAppleRefreshToken(loginTokenRequest.authorizationCode());
            savedMember.getOauthInformation().updateAppleRefreshToken(appleRefreshToken);
        }

        savedMember.upsertFirebaseToken(loginTokenRequest.firebaseToken());

        return jwtProvider.createTokenResponse(savedMember.getId(), savedMember.getOauthInformation(), savedMember.getRole(), secretKey);
    }

    private String requestAppleRefreshToken(String authorizationCode) {
        return oauthClientComposite.requestRefreshToken(OauthProvider.APPLE, authorizationCode);
    }

    public TokenResponse createTokenForTest(String email) {
        Member member = memberService.findByEmail(email);
        return jwtProvider.createTokenResponse(member.getId(), member.getOauthInformation(), member.getRole(), secretKey);
    }

    @Transactional
    public void logout(LoginUser loginUser) {
        boolean tokenExists = Boolean.TRUE.equals(oauthRedisTemplate.hasKey(loginUser.oauthInformation()));
        if(tokenExists) oauthRedisTemplate.delete(loginUser.oauthInformation());
    }

    @Transactional
    public void revoke(LoginUser loginUser) {
        log.info("revoke service for : {}", loginUser.oauthInformation().getOauthProviderId());
        Member member = memberService.findByOauthDetailInformation(loginUser.oauthInformation());
        log.info("member : {}", member.getOauthInformation().getOauthProviderId());
        oauthClientComposite.revoke(member.getOauthInformation().getOauthProvider(), member.getOauthInformation().getAppleRefreshToken());
        promissoryPaperService.removeRelation(member);
        memberService.delete(member);
    }

    public boolean checkAuthentication(LoginUser loginUser) {
        Member member = memberService.findByOauthInformation(loginUser.oauthInformation());
        boolean status = member.isAuthenticated();
        if(Boolean.FALSE.equals(status)) {
            throw new AuthException(AuthErrorCode.NOT_AUTHORIZED_MEMBER);
        }
        return true;
    }

    public TokenRefreshResponse refreshAccessToken(LoginTokenRequest loginTokenRequest) {
        String accessToken = jwtProvider.refreshAccessToken(loginTokenRequest.refreshToken(),secretKey);
        return new TokenRefreshResponse(accessToken, loginTokenRequest.refreshToken());
    }

    @Transactional
    public void initializeCertificationInformation(LoginUser loginUser, CertificationRequest certificationRequest) {
        // 1. 기존에 본인인증 한 적이 있는지 확인합니다.
        Member member = memberService.findById(loginUser.id());
        checkIfUserAlreadyAuthenticated(member.isAuthenticated());
        // 2. PortOne AccessToken을 요청합니다.

        PortOneTokenResponse portOneTokenResponse = getPortOneAccessToken();

        // 3. imp_uid를 통해 인증 된 정보를 가져옵니다.
        PortOneCertificationResponse portOneCertificationResponse = portOneApiClient.getCertificationInformation("Bearer %s".formatted(portOneTokenResponse.response().accessToken()), certificationRequest.impUid());
        CertificationInformation certificationInformation = portOneCertificationResponse.toEntity();

        // 4. 기존에 다른 아이디로 본인인증 된 적이 있는지 확인합니다.
        checkCertificationInformationExistence(certificationInformation.getName(), certificationInformation.getPhone());

        // 5. 유저의 본인인증 정보를 업데이트 합니다.
        member.updateCertificationInformation(certificationInformation);

    }

    private PortOneTokenResponse getPortOneAccessToken() {
        RestTemplate restTemplate = new RestTemplateBuilder().build();
        try {
            JSONObject formData = new JSONObject();
            formData.put("imp_key", portOneConfigProps.getAccessKey());
            formData.put("imp_secret", portOneConfigProps.getSecretKey());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> requestEntity = new HttpEntity<>(formData.toString(), headers);

            ResponseEntity<PortOneTokenResponse> responseEntity = restTemplate.postForEntity("https://api.iamport.kr/users/getToken", requestEntity, PortOneTokenResponse.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            log.error("Error occurred while getting PortOne access token", e);
        }
        return null;
    }

    public void checkCertificationInformationExistence(String name, String phone) {
        boolean exists = memberService.existsByCertificationInformation(name, phone);
        if (exists) {
            throw new AuthException(AuthErrorCode.IMMUTABLE_USER_AUTHENTICATED);
        }
    }

    public void checkIfUserAlreadyAuthenticated(boolean isAuthenticated) {
        if(isAuthenticated) {
            throw new AuthException(AuthErrorCode.ALREADY_USER_AUTHENTICATED);
        }
    }

}
