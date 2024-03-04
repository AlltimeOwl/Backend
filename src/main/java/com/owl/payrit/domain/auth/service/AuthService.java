package com.owl.payrit.domain.auth.service;

import com.owl.payrit.domain.auth.domain.OauthProvider;
import com.owl.payrit.domain.auth.dto.response.TokenResponse;
import com.owl.payrit.domain.auth.provider.OauthClientComposite;
import com.owl.payrit.domain.auth.util.JwtProvider;
import com.owl.payrit.domain.member.entity.Member;
import com.owl.payrit.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final OauthClientComposite oauthClientComposite;
    private final JwtProvider jwtProvider;

    @Value("${jwt.token.secret}")
    private String secretKey;

    @Transactional
    public TokenResponse login(OauthProvider oauthProvider, String accessToken) {

        Member member = oauthClientComposite.fetch(oauthProvider, accessToken);
        Member member1 = memberRepository.findByOauthInformation(member.getOauthInformation())
                                         .orElseGet(() -> memberRepository.save(member));

        return jwtProvider.createTokenResponse(member1.getId(), member1.getEmail(), member1.getRole(), secretKey);
    }
}
