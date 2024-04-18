package com.owl.payrit.domain.promise.service;

import com.owl.payrit.domain.auth.dto.response.LoginUser;
import com.owl.payrit.domain.member.entity.Member;
import com.owl.payrit.domain.member.service.MemberService;
import com.owl.payrit.domain.memo.dto.response.MemoListResponse;
import com.owl.payrit.domain.promise.dto.request.PromiseWriteRequest;
import com.owl.payrit.domain.promise.dto.response.PromiseDetailResponse;
import com.owl.payrit.domain.promise.dto.response.PromiseListResponse;
import com.owl.payrit.domain.promise.entity.Promise;
import com.owl.payrit.domain.promise.exception.PromiseErrorCode;
import com.owl.payrit.domain.promise.exception.PromiseException;
import com.owl.payrit.domain.promise.reposiroty.PromiseRepository;
import com.owl.payrit.domain.promissorypaper.dto.response.PaperListResponse;
import com.owl.payrit.domain.promissorypaper.entity.PaperRole;
import com.owl.payrit.global.utils.Ut;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<PromiseListResponse> getList(LoginUser loginUser) {

        Member loginedMember = memberService.findById(loginUser.id());

        String myName = memberService.getMyNameByMember(loginedMember);

        List<Promise> promises = promiseRepository.findAllByWriter(loginedMember);

        return promises.stream()
                .map(promise -> {
                    return new PromiseListResponse(promise, myName);
                })
                .collect(Collectors.toList());
    }

    public PromiseDetailResponse getDetail(LoginUser loginUser, Long promiseId) {

        Member loginedMember = memberService.findById(loginUser.id());

        Promise promise = getById(promiseId);
        String myName = memberService.getMyNameByMember(loginedMember);

        return new PromiseDetailResponse(promise, myName);
    }

    public Promise getById(Long promiseId) {

        Optional<Promise> OPromise = promiseRepository.findById(promiseId);

        if (OPromise.isEmpty()) {
            throw new PromiseException(PromiseErrorCode.PROMISE_NOT_FOUND);
        }

        return OPromise.get();
    }
}
