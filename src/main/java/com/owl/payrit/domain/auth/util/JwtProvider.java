package com.owl.payrit.domain.auth.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.owl.payrit.domain.auth.domain.OauthProvider;
import com.owl.payrit.domain.auth.dto.response.TokenResponse;
import com.owl.payrit.domain.member.entity.OauthInformation;
import com.owl.payrit.domain.member.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.jackson.io.JacksonDeserializer;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtProvider {

    private final ObjectMapper objectMapper;
    private final RedisTemplate<OauthInformation, String> redisTemplate;
    private final long accessTokenExpireTimeMs = 604800000L; // 1주일
    private final long refreshTokenExpireTimeMs = 1209600000L; // 2주일

    public Long getId(String token, String secretKey) {
        return Jwts.parserBuilder()
                   .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                   .build()
                   .parseClaimsJws(token)
                   .getBody()
                   .get("id", Long.class);
    }

    public String getEmail(String token, String secretKey) {
        return Jwts.parserBuilder()
                   .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                   .build()
                   .parseClaimsJws(token)
                   .getBody()
                   .get("email", String.class);
    }

    //TODO : 변경고민
    @SuppressWarnings("unchecked")
    public OauthInformation getOauthInformation(String token, String secretKey) {
        return parseOauthInformation(Jwts.parserBuilder()
                   .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                   .build()
                   .parseClaimsJws(token)
                   .getBody()
                   .get("oauthInformation", Map.class));
    }

    public boolean isExpired(String token, String secretKey) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                                        .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                                        .build()
                                        .parseClaimsJws(token);

            Date expirationDate = claimsJws.getBody().get("exp", Date.class);
            return expirationDate.before(new Date());
        } catch (JwtException e) {
            System.out.println(e.getMessage());
            return true;
        }
    }


    public String createToken(Long id, OauthInformation oauthInformation, Role role, String secretKey) {
        Claims claims = Jwts.claims();
        claims.put("id", id);
        claims.put("oauthInformation", oauthInformation);
        claims.put("role", role.name());

        Date now = new Date();
        Date accessTokenExpiration = new Date(now.getTime() + accessTokenExpireTimeMs);

        return Jwts.builder()
                   .setClaims(claims)
                   .setIssuedAt(now)
                   .setExpiration(accessTokenExpiration)
                   .signWith(
                       Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)),
                       SignatureAlgorithm.HS256)
                   .compact();

    }

    public String createRefreshToken(OauthInformation oauthInformation, String secretKey) {

        Claims claims = Jwts.claims();
        claims.put("oauthInformation", oauthInformation);

        Date now = new Date();
        Date refreshTokenExpiration = new Date(now.getTime() + refreshTokenExpireTimeMs);

        String refreshToken = Jwts.builder()
                                  .setClaims(claims)
                                  .setIssuedAt(now)
                                  .setExpiration(refreshTokenExpiration)
                                  .signWith(Keys.hmacShaKeyFor(
                                          secretKey.getBytes(StandardCharsets.UTF_8)),
                                      SignatureAlgorithm.HS256)
                                  .compact();
        redisTemplate.opsForValue()
                     .set(oauthInformation, refreshToken, refreshTokenExpireTimeMs, TimeUnit.MILLISECONDS);

        return refreshToken;

    }

    public TokenResponse createTokenResponse(Long id, OauthInformation oauthInformation, Role role, String secretKey) {
        String accessToken = createToken(id, oauthInformation, role, secretKey);
        String refreshToken = createRefreshToken(oauthInformation, secretKey);
        return new TokenResponse(id, accessToken, refreshToken);
    }

    public String refreshAccessToken(String refreshToken, String secretKey) {
        try {
            Claims claims = Jwts.parserBuilder()
                                .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                                .deserializeJsonWith(new JacksonDeserializer<>(objectMapper))
                                .build()
                                .parseClaimsJws(refreshToken)
                                .getBody();
            //TODO : 로직 변경 고려
            OauthInformation oauthInformation = parseOauthInformation(claims.get("oauthInformation", Map.class));

            // Redis 또는 데이터베이스에서 저장된 refreshToken과 전달된 refreshToken이 일치하는지 확인
            String storedRefreshToken = redisTemplate.opsForValue()
                                                     .get(oauthInformation);

            if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
                throw new RuntimeException();
            }

            // 저장된 refreshToken이 유효하면, 새로운 Access Token을 발급
            Date now = new Date();
            Date accessTokenExpiration = new Date(now.getTime() + accessTokenExpireTimeMs);

            Claims newClaims = Jwts.claims();
            newClaims.put("oauthInformation", oauthInformation);

            return Jwts.builder()
                       .setClaims(newClaims)
                       .setIssuedAt(now)
                       .setExpiration(accessTokenExpiration)
                       .signWith(Keys.hmacShaKeyFor(
                               secretKey.getBytes(StandardCharsets.UTF_8)),
                           SignatureAlgorithm.HS256)
                       .compact();

        } catch (Exception e) {
            // Refresh Token 검증 실패
            System.out.println(e.getMessage());
            throw new RuntimeException();
        }
    }


    private OauthInformation parseOauthInformation(Map<String, Object> map) {
        // Map에서 필요한 정보를 추출하여 OauthInformation 객체를 생성하는 로직
        String oauthProviderId = (String) map.get("oauthProviderId");
        OauthProvider oauthProvider = OauthProvider.valueOf((String) map.get("oauthProvider"));
        return new OauthInformation(oauthProviderId, oauthProvider);
    }

}
