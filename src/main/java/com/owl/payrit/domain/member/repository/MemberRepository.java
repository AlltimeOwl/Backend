package com.owl.payrit.domain.member.repository;

import com.owl.payrit.domain.member.entity.Member;
import com.owl.payrit.domain.member.entity.OauthInformation;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByOauthInformation(OauthInformation oauthInformation);
    Optional<Member> findByEmail(String email);
    Optional<Member> findByPhoneNumber(String phoneNumber);
    Optional<Member> findByNameAndPhoneNumber(String name, String phoneNumber);
    boolean existsByNameAndPhoneNumber(String name, String phoneNumber);
}
