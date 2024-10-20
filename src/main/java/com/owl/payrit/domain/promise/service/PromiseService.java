package com.owl.payrit.domain.promise.service;

import com.owl.payrit.domain.auth.dto.response.LoginUser;
import com.owl.payrit.domain.docsinfo.config.AzureStorageConfigProps;
import com.owl.payrit.domain.member.entity.Member;
import com.owl.payrit.domain.member.service.MemberService;
import com.owl.payrit.domain.promise.dto.request.PromiseWriteRequest;
import com.owl.payrit.domain.promise.dto.response.PromiseDetailResponse;
import com.owl.payrit.domain.promise.dto.response.PromiseListResponse;
import com.owl.payrit.domain.promise.entity.ParticipantsInfo;
import com.owl.payrit.domain.promise.entity.Promise;
import com.owl.payrit.domain.promise.entity.PromiseImageType;
import com.owl.payrit.domain.promise.exception.PromiseErrorCode;
import com.owl.payrit.domain.promise.exception.PromiseException;
import com.owl.payrit.domain.promise.repository.PromiseRepository;
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

    private final AzureStorageConfigProps azureStorageConfigProps;
    private final MemberService memberService;
    private final PromiseRepository promiseRepository;

    @Transactional
    public Long write(LoginUser loginUser, PromiseWriteRequest request) {

        Member loginedMember = memberService.findById(loginUser.id());

        Promise promise = Promise.builder()
                .owner(loginedMember)
                .writer(loginedMember)
                .writerName(request.writerName())
                .amount(request.amount())
                .promiseStartDate(request.promiseStartDate())
                .promiseEndDate(request.promiseEndDate())
                .contents(request.contents())
                .participants(getParticipantsInfoListByReq(request))
                .promiseImageType(request.promiseImageType())
                .build();

        Promise savedPromise = promiseRepository.save(promise);

        return savedPromise.getId();
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

        List<Promise> promises = promiseRepository.findAllByOwner(loginedMember);

        return promises.stream()
                .map(PromiseListResponse::new)
                .collect(Collectors.toList());
    }

    public PromiseDetailResponse getDetail(LoginUser loginUser, Long promiseId) {

        Member loginedMember = memberService.findById(loginUser.id());

        Promise promise = getById(promiseId);

        if (!promise.getWriter().equals(loginedMember) && !promise.getOwner().equals(loginedMember)) {
            throw new PromiseException(PromiseErrorCode.PROMISE_IS_NOT_MINE);
        }

        return new PromiseDetailResponse(promise);
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

        if (!promise.getOwner().equals(loginedMember)) {
            throw new PromiseException(PromiseErrorCode.PROMISE_IS_NOT_MINE);
        }

        promiseRepository.delete(promise);
    }

    @Transactional
    public void removeAllByRevoke(Member member) {

        List<Promise> promises = promiseRepository.findAllByWriter(member);

        for (Promise promise : promises) {
            promise.removeRelation();
        }

        promiseRepository.deleteAll(promises);
    }

    @Transactional
    public void share(LoginUser loginUser, Long promiseId) {

        Member loginedMember = memberService.findById(loginUser.id());

        Promise existPromise = getById(promiseId);

        Promise promise = Promise.builder()
                .owner(loginedMember)
                .writer(existPromise.getWriter())
                .writerName(existPromise.getWriterName())
                .amount(existPromise.getAmount())
                .promiseStartDate(existPromise.getPromiseStartDate())
                .promiseEndDate(existPromise.getPromiseEndDate())
                .contents(existPromise.getContents())
                .participants(new ArrayList<>(existPromise.getParticipants()))
                .promiseImageType(existPromise.getPromiseImageType())
                .build();

        promiseRepository.save(promise);
    }
}
