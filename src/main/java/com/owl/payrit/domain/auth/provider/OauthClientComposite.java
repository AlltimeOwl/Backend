package com.owl.payrit.domain.auth.provider;

import com.owl.payrit.domain.auth.domain.OauthProvider;
import com.owl.payrit.domain.member.entity.Member;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class OauthClientComposite {

    private final Map<OauthProvider, OauthClient> mapping;

    public OauthClientComposite(Set<OauthClient> clients) {
        mapping = clients.stream()
                         .collect(
                             Collectors.toMap(
                                 OauthClient::oauthProvider,
                                 Function.identity()
                             )
                         );
    }

    public Member fetch(OauthProvider oauthProvider, String accessToken) {
        return getProvider(oauthProvider).fetch(accessToken);
    }

    private OauthClient getProvider(OauthProvider oauthProvider) {
        return Optional.ofNullable(mapping.get(oauthProvider))
            .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 소셜 로그인입니다."));
    }
}
