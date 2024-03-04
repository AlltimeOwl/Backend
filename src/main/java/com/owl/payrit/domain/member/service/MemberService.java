package com.owl.payrit.domain.member.service;

import com.owl.payrit.domain.member.entity.Member;
import com.owl.payrit.domain.member.exception.MemberException;
import com.owl.payrit.domain.member.repository.MemberRepository;
import com.owl.payrit.global.exception.ErrorCode;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
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

    /*
    차용증 작성 시, 상대방이 가입되어있지 않을 상황을 고려해 Optional<Member> 반환
     */
    public Optional<Member> findByPhoneNumberForPromissory(String phoneNumber) {
        return memberRepository.findByPhoneNumber(phoneNumber);
    }

}
