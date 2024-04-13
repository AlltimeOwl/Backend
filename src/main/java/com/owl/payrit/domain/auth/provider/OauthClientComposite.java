package com.owl.payrit.domain.auth.provider;

import com.owl.payrit.domain.auth.domain.OauthProvider;
import com.owl.payrit.domain.auth.dto.request.LoginTokenRequest;
import com.owl.payrit.domain.member.entity.Member;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class OauthClientComposite {

    private final EnumMap<OauthProvider, OauthClient> mapping;

    public OauthClientComposite(Set<OauthClient> clients) {
        mapping = clients.stream()
                         .collect(
                             Collectors.toMap(
                                 OauthClient::oauthProvider,
                                 Function.identity(),
                                 (existing, replacement) -> existing,
                                 () -> new EnumMap<OauthProvider, OauthClient>(OauthProvider.class)
                             )
                         );
    }

    public Member fetch(OauthProvider oauthProvider, LoginTokenRequest loginTokenRequest) {
        return getProvider(oauthProvider).fetch(loginTokenRequest);
    }

    private OauthClient getProvider(OauthProvider oauthProvider) {
        return Optional.ofNullable(mapping.get(oauthProvider))
            .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 소셜 로그인입니다."));
    }

    public void revoke(OauthProvider oauthProvider, String oauthCode) {
        getProvider(oauthProvider).revoke(oauthCode);
    }

    public String requestRefreshToken(OauthProvider oauthProvider, String oauthCode) {
        return getProvider(oauthProvider).requestRefreshToken(oauthCode);
    }
}
