package com.owl.payrit.domain.promissorypaper.service;

import com.owl.payrit.domain.auth.dto.response.LoginUser;
import com.owl.payrit.domain.member.entity.Member;
import com.owl.payrit.domain.member.service.MemberService;
import com.owl.payrit.domain.promissorypaper.dto.request.PaperWriteRequest;
import com.owl.payrit.domain.promissorypaper.dto.response.PaperDetailResponse;
import com.owl.payrit.domain.promissorypaper.dto.response.PaperListResponse;
import com.owl.payrit.domain.promissorypaper.entity.PromissoryPaper;
import com.owl.payrit.domain.promissorypaper.exception.PromissoryPaperException;
import com.owl.payrit.domain.promissorypaper.repository.PromissoryPaperRepository;
import com.owl.payrit.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PromissoryPaperService {

    private final MemberService memberService;
    private final PromissoryPaperRepository promissoryPaperRepository;

    @Transactional
    public void writePaper(LoginUser loginUser, PaperWriteRequest paperWriteRequest) {

        Member loginedMember = memberService.findById(loginUser.id());

        //FIXME: 상대방이 회원가입 하지 않은 상황이면, 조회가 불가능? => 일단 null로 반환함.
        Member creditor = memberService.findByPhoneNumberForPromissory(
                paperWriteRequest.creditorPhoneNumber()).orElse(null);
        Member debtor = memberService.findByPhoneNumberForPromissory(
                paperWriteRequest.debtorPhoneNumber()).orElse(null);

        //TODO: 폼의 입력한 데이터(채권자, 채무자)중 내 정보와 일치하는 내용이 반드시 있어야 한다.

        //TODO: 동일한 데이터로 차용증을 2건 작성하려 한다면 막는 기능이 필요할 수 있다.

        PromissoryPaper paper = PromissoryPaper.builder()
                .amount(paperWriteRequest.amount())
                .transactionDate(paperWriteRequest.transactionDate())
                .repaymentStartDate(paperWriteRequest.repaymentStartDate())
                .repaymentEndDate(paperWriteRequest.repaymentEndDate())
                .specialConditions(paperWriteRequest.specialConditions())
                .interestRate(paperWriteRequest.interestRate())
                .writer(loginedMember)
                .creditor(creditor)
                .creditorName(paperWriteRequest.creditorName())
                .creditorPhoneNumber(paperWriteRequest.creditorPhoneNumber())
                .creditorAddress(paperWriteRequest.creditorAddress())
                .isCreditorAgree(loginedMember.equals(creditor))
                .debtor(debtor)
                .debtorName(paperWriteRequest.debtorName())
                .debtorPhoneNumber(paperWriteRequest.debtorPhoneNumber())
                .debtorAddress(paperWriteRequest.debtorAddress())
                .isDebtorAgree(loginedMember.equals(debtor))
                .paperKey(getRandomKey())
                .storageUrl(null)           //FIXME: 추후 저장소 URL로 저장 필요
                .build();

        promissoryPaperRepository.save(paper);
    }

    private String getRandomKey() {

        String paperKey = UUID.randomUUID().toString();
        return promissoryPaperRepository.existsByPaperKey(paperKey) ? getRandomKey() : paperKey;
    }

    public PaperDetailResponse getDetail(LoginUser loginUser, Long paperId) {

        PromissoryPaper promissoryPaper = promissoryPaperRepository.findById(paperId).orElseThrow(
                () -> new PromissoryPaperException(ErrorCode.PAPER_NOT_FOUND));

        if (!isMine(loginUser.id(), promissoryPaper)) {
            throw new PromissoryPaperException(ErrorCode.PAPER_IS_NOT_MINE);
        }

        return new PaperDetailResponse(promissoryPaper);
    }

    private boolean isMine(Long memberId, PromissoryPaper promissoryPaper) {

        if (promissoryPaper.getCreditor().getId().equals(memberId)
                || promissoryPaper.getDebtor().getId().equals(memberId)) {
            return true;
        }

        return false;
    }

    public Map<String, List<PaperListResponse>> getListResponses(LoginUser loginUser) {

        Member loginedMember = memberService.findById(loginUser.id());

        Map<String, List<PaperListResponse>> listResponseMap = new HashMap<>();

        List<PromissoryPaper> creditorPaperList = promissoryPaperRepository.findAllByCreditor(loginedMember);
        List<PaperListResponse> creditorResponseList = new ArrayList<>();

        for (PromissoryPaper paper : creditorPaperList) {
            creditorResponseList.add(new PaperListResponse(paper, paper.getDebtorName(), calcDueDate(paper)));
            listResponseMap.put("creditor", creditorResponseList);
        }

        List<PromissoryPaper> debtorPaperList = promissoryPaperRepository.findAllByDebtor(loginedMember);
        List<PaperListResponse> debtorResponseList = new ArrayList<>();

        for (PromissoryPaper paper : debtorPaperList) {
            debtorResponseList.add(new PaperListResponse(paper, paper.getCreditorName(), calcDueDate(paper)));
            listResponseMap.put("debtor", debtorResponseList);
        }

        return listResponseMap;
    }

    private long calcDueDate(PromissoryPaper paper) {

        LocalDate today = LocalDate.now();
        LocalDate repaymentEndDate = paper.getRepaymentEndDate();

        return ChronoUnit.DAYS.between(today, repaymentEndDate);
    }
}