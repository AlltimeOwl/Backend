package com.owl.payrit.domain.auth.provider.apple;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AppleJwtValidator {

    private final ObjectMapper objectMapper;

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
}
