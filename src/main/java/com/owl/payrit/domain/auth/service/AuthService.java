package com.owl.payrit.domain.auth.service;

import com.owl.payrit.domain.auth.domain.OauthProvider;
import com.owl.payrit.domain.auth.dto.request.LoginTokenRequest;
import com.owl.payrit.domain.auth.dto.request.RevokeRequest;
import com.owl.payrit.domain.auth.dto.response.LoginUser;
import com.owl.payrit.domain.auth.dto.response.TokenRefreshResponse;
import com.owl.payrit.domain.auth.dto.response.TokenResponse;
import com.owl.payrit.domain.auth.exception.AuthErrorCode;
import com.owl.payrit.domain.auth.exception.AuthException;
import com.owl.payrit.domain.auth.provider.OauthClientComposite;
import com.owl.payrit.domain.auth.util.JwtProvider;
import com.owl.payrit.domain.member.entity.Member;
import com.owl.payrit.domain.member.entity.OauthInformation;
import com.owl.payrit.domain.member.service.MemberService;
import com.owl.payrit.domain.promissorypaper.service.PromissoryPaperService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Value("${jwt.token.secret}")
    private String secretKey;

    @Transactional
    public TokenResponse login(OauthProvider oauthProvider, LoginTokenRequest loginTokenRequest) {

        Member memberInformation = oauthClientComposite.fetch(oauthProvider, loginTokenRequest.accessToken());
        Member savedMember = memberService.findByOauthInformationOrSave(memberInformation);
        savedMember.upsertFirebaseToken(loginTokenRequest.firebaseToken());
        // TODO : 새로 만들었다면, 차용증 매핑
        return jwtProvider.createTokenResponse(savedMember.getId(), savedMember.getOauthInformation(), savedMember.getRole(), secretKey);
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
    public void revoke(LoginUser loginUser, RevokeRequest revokeRequest) {
        log.info("revoke service for : {}", loginUser.oauthInformation().getOauthProviderId());
        Member member = memberService.findByOauthInformation(loginUser.oauthInformation());
        log.info("member : {}", member.getOauthInformation().getOauthProviderId());
        oauthClientComposite.revoke(member.getOauthInformation().getOauthProvider(), revokeRequest.oauthCode());
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
}
