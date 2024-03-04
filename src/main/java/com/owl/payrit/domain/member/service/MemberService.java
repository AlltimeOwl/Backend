package com.owl.payrit.domain.member.service;

import com.owl.payrit.domain.member.entity.Member;
import com.owl.payrit.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public Member getById(long id) {
        Optional<Member> OById = memberRepository.findById(id);

        //TODO: Optional 예외처리
        
        return OById.get();
    }

    public Member getByPhoneNumber(String phoneNumber) {
        Optional<Member> OByPhoneNumber = memberRepository.findByPhoneNumber(phoneNumber);

        //TODO: Optional 예외 처리

        return OByPhoneNumber.orElse(null);
    }
}
