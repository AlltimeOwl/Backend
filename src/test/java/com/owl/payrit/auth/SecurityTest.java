package com.owl.payrit.auth;

import com.owl.payrit.domain.auth.domain.OauthProvider;
import com.owl.payrit.domain.auth.provider.OauthClientComposite;
import com.owl.payrit.domain.auth.util.JwtProvider;
import com.owl.payrit.domain.member.entity.Member;
import com.owl.payrit.domain.member.entity.OauthInformation;
import com.owl.payrit.global.testutil.FakeKakaoMemberClient;
import com.owl.payrit.util.AbstractContainerTest;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
public class SecurityTest extends AbstractContainerTest {

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private RedisTemplate<OauthInformation, String> redisTemplate;

    @Value("${jwt.token.secret}")
    private String secretKey;

    private Member createMember() {
        // 테스트를 위해 FakeKakaoMemberClient를 생성하여 사용
        OauthClientComposite oauthClientComposite = new OauthClientComposite(Set.of(new FakeKakaoMemberClient()));

        return oauthClientComposite.fetch(OauthProvider.KAKAO, "fake_access_token");
    }

    @DisplayName("JWT 생성된다")
    @Test
    void JWT_token_generated() throws Exception {

        Member member = createMember();

        String token = jwtProvider.createToken(member.getId(), member.getOauthInformation(), member.getRole(), secretKey);

        assertThat(token).satisfies(t -> {
            assertThat(t).isNotNull();
            assertThat(t).isNotBlank();
            assertThat(t).isNotEmpty();
        });
    }

    @DisplayName("JWT로 payload 조회된다")
    @Test
    void getPayloadByJWT() throws Exception {

        Member member = createMember();
        OauthInformation oauthInformation = member.getOauthInformation();
        String token = jwtProvider.createToken(member.getId(), member.getOauthInformation(), member.getRole(), secretKey);

        assertThat(member).satisfies(u -> {
            assertThat(u.getOauthInformation()).isEqualTo(oauthInformation);
        });
    }

    @DisplayName("만료된 JWT 판별된다")
    @Test
    void expiredTokenDetected() throws Exception {

        final String expiredToken = Jwts.builder()
                                        .signWith(Keys.hmacShaKeyFor(
                                                secretKey.getBytes(StandardCharsets.UTF_8)),
                                            SignatureAlgorithm.HS256)
                                        .setExpiration(new Date(new Date().getTime() - 1))
                                        .compact();

        assertThat(jwtProvider.isExpired(expiredToken, secretKey)).isEqualTo(true);
    }

    @Transactional
    @DisplayName("REFRESH TOKEN 생성된다")
    @Test
    void refreshTokenGenerated() throws Exception {

        String refreshToken = jwtProvider.createRefreshToken(new OauthInformation("test", OauthProvider.KAKAO), secretKey);

        assertThat(refreshToken).satisfies(t -> {
            assertThat(t).isNotNull();
            assertThat(t).isNotBlank();
        });
    }

    @Transactional
    @DisplayName("ReFresh Token Redis에 저장된다")
    @Test
    void refreshTokenIsStoredInRedis() throws Exception {

        jwtProvider.createRefreshToken(new OauthInformation("test", OauthProvider.KAKAO), secretKey);
        System.out.println(redisTemplate.opsForValue().get("email"));
        boolean tokenExists = Boolean.TRUE.equals(redisTemplate.hasKey(new OauthInformation("test", OauthProvider.KAKAO)));

        assertThat(tokenExists).isTrue();

    }

    @Transactional
    @DisplayName("Token Refersh된다")
    @Test
    void tokenIsRefreshed() throws Exception {

        String refreshToken = jwtProvider.createRefreshToken(new OauthInformation("test", OauthProvider.KAKAO), secretKey);
        String accessToken = jwtProvider.refreshAccessToken(refreshToken, secretKey);

        assertThat(accessToken).isNotNull();
    }
}
