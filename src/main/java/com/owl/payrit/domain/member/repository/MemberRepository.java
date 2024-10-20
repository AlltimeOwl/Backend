package com.owl.payrit.domain.member.repository;

import com.owl.payrit.domain.auth.domain.OauthProvider;
import com.owl.payrit.domain.member.entity.Member;
import com.owl.payrit.domain.member.entity.OauthInformation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByOauthInformation(OauthInformation oauthInformation);
    Optional<Member> findByOauthInformationOauthProviderIdAndOauthInformationOauthProvider(String oauthProviderId, OauthProvider oauthProvider);
    Optional<Member> findByEmail(String email);
    Optional<Member> findByPhoneNumber(String phoneNumber);
    Optional<Member> findByNameAndPhoneNumber(String name, String phoneNumber);
    boolean existsByNameAndPhoneNumber(String name, String phoneNumber);
    boolean existsByCertificationInformationNameAndCertificationInformationPhone(String name, String phone);
    Optional<Member> findByCertificationInformationPhone(String phone);
}
