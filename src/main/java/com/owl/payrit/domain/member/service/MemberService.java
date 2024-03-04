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
}
