package com.owl.payrit.domain.promise.service;

import com.owl.payrit.domain.auth.dto.response.LoginUser;
import com.owl.payrit.domain.member.entity.Member;
import com.owl.payrit.domain.member.service.MemberService;
import com.owl.payrit.domain.promise.dto.request.PromiseWriteRequest;
import com.owl.payrit.domain.promise.dto.response.PromiseDetailResponse;
import com.owl.payrit.domain.promise.dto.response.PromiseListResponse;
import com.owl.payrit.domain.promise.entity.ParticipantsInfo;
import com.owl.payrit.domain.promise.entity.Promise;
import com.owl.payrit.domain.promise.exception.PromiseErrorCode;
import com.owl.payrit.domain.promise.exception.PromiseException;
import com.owl.payrit.domain.promise.reposiroty.PromiseRepository;
import com.owl.payrit.global.utils.Ut;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
                .participants(getParticipantsInfoListByReq(request))
                .build();

        promiseRepository.save(promise);
    }

    private List<ParticipantsInfo> getParticipantsInfoListByReq(PromiseWriteRequest request) {

        List<ParticipantsInfo> infoList = new ArrayList<>();
        List<String> nameList = Ut.str.parsedParticipants(request.participantsName());
        List<String> phoneList = Ut.str.parsedParticipants(request.participantsPhone());

        if (nameList.size() != phoneList.size()) {
            throw new PromiseException(PromiseErrorCode.PROMISE_PARTICIPANTS_SIZE);
        }

        for (int i = 0; i < nameList.size(); i++) {
            ParticipantsInfo participantsInfo = new ParticipantsInfo(nameList.get(i), phoneList.get(i));
            infoList.add(participantsInfo);
        }

        return infoList;
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

        if (!promise.getWriter().equals(loginedMember)) {
            throw new PromiseException(PromiseErrorCode.PROMISE_IS_NOT_MINE);
        }

        return new PromiseDetailResponse(promise, myName);
    }

    public Promise getById(Long promiseId) {

        Optional<Promise> OPromise = promiseRepository.findById(promiseId);

        if (OPromise.isEmpty()) {
            throw new PromiseException(PromiseErrorCode.PROMISE_NOT_FOUND);
        }

        return OPromise.get();
    }

    @Transactional
    public void remove(LoginUser loginUser, Long promiseId) {

        Member loginedMember = memberService.findById(loginUser.id());

        Promise promise = getById(promiseId);

        if (!promise.getWriter().equals(loginedMember)) {
            throw new PromiseException(PromiseErrorCode.PROMISE_IS_NOT_MINE);
        }

        promiseRepository.delete(promise);
    }
}
