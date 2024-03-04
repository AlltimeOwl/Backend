package com.owl.payrit.domain.auth.service;

import com.owl.payrit.domain.auth.domain.OauthProvider;
import com.owl.payrit.domain.auth.dto.response.TokenResponse;
import com.owl.payrit.domain.auth.provider.OauthClientComposite;
import com.owl.payrit.domain.auth.util.JwtProvider;
import com.owl.payrit.domain.member.entity.Member;
import com.owl.payrit.domain.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
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

        Member kakaoMemberInformation = oauthClientComposite.fetch(oauthProvider, accessToken);
        Member savedMember = memberRepository.findByOauthInformation(kakaoMemberInformation.getOauthInformation())
                                         .orElseGet(() -> memberRepository.save(kakaoMemberInformation));
        // 새로 만들었다면, 차용증 매핑
        return jwtProvider.createTokenResponse(savedMember.getId(), savedMember.getEmail(), savedMember.getRole(), secretKey);
    }


    private void verifyDuplicated(Member kakaoMemberInformation) {
        /*
        1. 회원가입이 되어있는가?
        2. 되어있다면 -> 그 아이디로 로그인
        3. 안 되어 있다면
        3-1. -> 같은 이름, 핸드폰 번호로 가입한 아이디가 있는가?
        -> 있다면, 그 아이디로 로그인? 알려주기?
        -> 없다면, 회원가입 시키고 로그인
         */
        Optional<Member> candidate = memberRepository.findByOauthInformation(kakaoMemberInformation.getOauthInformation());

        if(candidate.isEmpty()) {
            boolean exists = memberRepository.existsByNameAndPhoneNumber(kakaoMemberInformation.getName(), kakaoMemberInformation.getPhoneNumber());
            if(exists) {
                throw new RuntimeException();
            }
        }



    }

    public TokenResponse createTokenForTest(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
        return jwtProvider.createTokenResponse(member.getId(), member.getEmail(), member.getRole(), secretKey);
    }
}
