package com.owl.payrit.domain.auth.provider.apple;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.owl.payrit.domain.auth.dto.request.AppleRevokeRequest;
import com.owl.payrit.domain.auth.dto.request.AppleTokenGenerateRequest;
import com.owl.payrit.domain.auth.exception.AuthErrorCode;
import com.owl.payrit.domain.auth.exception.AuthException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class AppleJwtValidator {

    private final ObjectMapper objectMapper;
    @Value("${oauth.apple.appleKeyId}")
    private String appleKeyId;
    @Value("${oauth.apple.appleTeamId}")
    private String appleTeamId;
    @Value("${oauth.apple.appleBundleId}")
    private String appleBundleId;
    @Value("${oauth.apple.classpath}")
    private String keyClassPath;


    public Map<String, String> parseHeaders(String token){
        try {
            String header = token.split("\\.")[0];
            return objectMapper.readValue(decodeHeader(header), Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException();
        }
    }

    public String decodeHeader(String header) {
        return new String(Base64.getDecoder().decode(header), StandardCharsets.UTF_8);
    }

    public Claims getTokenClaims(String token, PublicKey publicKey) {
        return Jwts.parserBuilder()
                   .setSigningKey(publicKey)
                   .build()
                   .parseClaimsJws(token)
                   .getBody();
    }

    public String createClientSecret() throws IOException{
        log.info("CreateClientSecret");
        LocalDateTime issuedTime = LocalDateTime.now().plusMinutes(10L);
        Map<String, Object> jwtHeader = new HashMap<>();
        jwtHeader.put("alg", "ES256");
        jwtHeader.put("kid", appleKeyId);

        return Jwts.builder()
                   .setHeaderParams(jwtHeader)
                   .setIssuer(appleTeamId)
                   .setIssuedAt(new Date(System.currentTimeMillis())) // 발행 시간 - UNIX 시간
                   .setExpiration(Date.from(issuedTime.atZone(ZoneId.systemDefault()).toInstant())) // 만료 시간
                   .setAudience("https://appleid.apple.com")
                   .setSubject(appleBundleId)
                   .signWith(generatePrivateKey(), SignatureAlgorithm.ES256)
                   .compact();
    }

    private PrivateKey generatePrivateKey() throws IOException {
        String privateKey = null;
        try (InputStream inputStream = getClass().getResourceAsStream(keyClassPath)) {
            if (inputStream == null) {
                throw new FileNotFoundException("Resource not found: " + keyClassPath);
            }
            privateKey = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            log.info("private key : {}" , privateKey);
        }

        Reader pemReader = new StringReader(privateKey);
        PEMParser pemParser = new PEMParser(pemReader);
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        PrivateKeyInfo object = (PrivateKeyInfo) pemParser.readObject();
        return converter.getPrivateKey(object);
    }

    public AppleTokenGenerateRequest generateAppleToken(String authorizationCode) {
        log.info("AppleTokenGenerate code : {}", authorizationCode);
        AppleTokenGenerateRequest appleTokenGenerateRequest = null;
        try {
            String clientSecret = createClientSecret();
            appleTokenGenerateRequest = new AppleTokenGenerateRequest(appleBundleId, clientSecret, authorizationCode);
            return appleTokenGenerateRequest;
        } catch (Exception e) {
            throw new AuthException(AuthErrorCode.FILE_PATH_ERROR);
        }
    }

    public AppleRevokeRequest generateAppleRevokeRequest(String accessToken) {
        AppleRevokeRequest appleRevokeRequest = null;
        try {
            String clientSecret = createClientSecret();
            appleRevokeRequest = new AppleRevokeRequest(appleBundleId, clientSecret, accessToken);
            return appleRevokeRequest;
        } catch (Exception e) {
            throw new AuthException(AuthErrorCode.FILE_PATH_ERROR);
        }
    }
}
