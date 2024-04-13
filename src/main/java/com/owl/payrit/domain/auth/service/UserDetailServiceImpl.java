package com.owl.payrit.domain.auth.service;

import com.owl.payrit.domain.member.entity.OauthInformation;
import com.owl.payrit.domain.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByEmail(username).orElseThrow(EntityNotFoundException::new);
    }

    public UserDetails loadUserByOauthInformation(OauthInformation oauthInformation) {
        return memberRepository.findByOauthInformationOauthProviderIdAndOauthInformationOauthProvider(oauthInformation.getOauthProviderId(), oauthInformation.getOauthProvider()).orElseThrow(EntityNotFoundException::new);
    }
}
