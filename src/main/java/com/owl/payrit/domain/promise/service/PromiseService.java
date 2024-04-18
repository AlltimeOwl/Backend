package com.owl.payrit.domain.promise.service;

import com.owl.payrit.domain.auth.dto.response.LoginUser;
import com.owl.payrit.domain.member.entity.Member;
import com.owl.payrit.domain.member.service.MemberService;
import com.owl.payrit.domain.promise.dto.request.PromiseWriteRequest;
import com.owl.payrit.domain.promise.entity.Promise;
import com.owl.payrit.domain.promise.reposiroty.PromiseRepository;
import com.owl.payrit.global.utils.Ut;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PromiseService {

    private final MemberService memberService;

    private final PromiseRepository promiseRepository;

    @Transactional
    public void write(LoginUser loginUser, PromiseWriteRequest request) {

        Member loginedMember = memberService.findById(loginUser.id());

        Promise promise = Promise.builder()
                .writer(loginedMember)
                .amount(request.amount())
                .promiseEndDate(request.promiseEndDate())
                .contents(request.contents())
                .participants(Ut.str.parsedParticipants(request.participants()))
                .build();

        promiseRepository.save(promise);
    }
}
