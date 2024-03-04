package com.owl.payrit.domain.member.service;

import com.owl.payrit.domain.member.entity.Member;
import com.owl.payrit.domain.member.exception.MemberException;
import com.owl.payrit.domain.member.repository.MemberRepository;
import com.owl.payrit.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public Member findById(long id) {

        return memberRepository.findById(id)
                               .orElseThrow(() -> new MemberException(
                                   ErrorCode.MEMBER_NOT_FOUND));
    }

    public Member findByPhoneNumber(String phoneNumber) {

        return memberRepository.findByPhoneNumber(phoneNumber)
                               .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));
    }
}
