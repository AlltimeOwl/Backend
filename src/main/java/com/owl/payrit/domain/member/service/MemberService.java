package com.owl.payrit.domain.member.service;

import com.owl.payrit.domain.auth.dto.response.LoginUser;
import com.owl.payrit.domain.member.entity.Member;
import com.owl.payrit.domain.member.entity.OauthInformation;
import com.owl.payrit.domain.member.exception.MemberErrorCode;
import com.owl.payrit.domain.member.exception.MemberException;
import com.owl.payrit.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public Member findById(long id) {

        return memberRepository.findById(id)
                               .orElseThrow(() -> new MemberException(
                                   MemberErrorCode.MEMBER_NOT_FOUND));
    }

    public Member findByPhoneNumber(String phoneNumber) {

        return memberRepository.findByPhoneNumber(phoneNumber)
                               .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                               .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    /*
    차용증 작성 시, 상대방이 가입되어있지 않을 상황을 고려해 Optional<Member> 반환
     */
    public Optional<Member> findByPhoneNumberForPromissory(String phoneNumber) {
        return memberRepository.findByPhoneNumber(phoneNumber);
    }

    public Member findByOauthInformation(OauthInformation oauthInformation) {
        return memberRepository.findByOauthInformation(oauthInformation)
                               .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    public Member findByOauthInformationOrSave(Member member) {
        return memberRepository.findByOauthInformation(member.getOauthInformation())
                               .orElseGet(() -> memberRepository.save(member));
    }

    public void modifyAlarmStatus(LoginUser loginUser) {
        Member member = findByOauthInformation(loginUser.oauthInformation());
        member.modifyAlarmStatus();
    }

    public void delete(Member member) {
        memberRepository.delete(member);
    }

    public boolean existsByCertificationInformation(String name, String phone) {
        return memberRepository.existsByCertificationInformationNameAndCertificationInformationPhone(name, phone);
    }
}
