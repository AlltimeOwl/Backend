package com.owl.payrit.domain.promissorypaper.service;

import com.owl.payrit.domain.auth.dto.response.LoginUser;
import com.owl.payrit.domain.member.entity.Member;
import com.owl.payrit.domain.member.service.MemberService;
import com.owl.payrit.domain.promissorypaper.dto.request.PaperWriteRequest;
import com.owl.payrit.domain.promissorypaper.entity.PromissoryPaper;
import com.owl.payrit.domain.promissorypaper.repository.PromissoryPaperRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PromissoryPaperService {

    private final MemberService memberService;
    private final PromissoryPaperRepository promissoryPaperRepository;

    @Transactional
    public void writePaper(LoginUser loginUser, PaperWriteRequest paperWriteRequest) {

        Member loginedMember = memberService.getById(loginUser.id());

        //FIXME: 상대방이 회원가입 하지 않은 상황이면, 조회가 불가능
        Member creditor = memberService.getByPhoneNumber(paperWriteRequest.creditorPhoneNumber());
        Member debtor = memberService.getByPhoneNumber(paperWriteRequest.debtorPhoneNumber());

        //TODO: 폼 입력 데이터와 문서 입력 데이터가 일치하는지 검사 필요??

        PromissoryPaper paper = PromissoryPaper.builder()
                .amount(paperWriteRequest.amount())
                .transactionDate(paperWriteRequest.transactionDate())
                .repaymentStartDate(paperWriteRequest.repaymentStartDate())
                .repaymentEndDate(paperWriteRequest.repaymentEndDate())
                .specialConditions(paperWriteRequest.specialConditions())
                .interestRate(paperWriteRequest.interestRate())
                .writer(loginedMember)
                .creditor(creditor)
                .creditorPhoneNumber(paperWriteRequest.creditorPhoneNumber())
                .creditorAddress(paperWriteRequest.creditorAddress())
                .isCreditorAgree(false)     //FIXME 작성자가 누군지에 따라 동의 여부 설정 필요
                .debtor(debtor)
                .debtorPhoneNumber(paperWriteRequest.debtorPhoneNumber())
                .debtorAddress(paperWriteRequest.debtorAddress())
                .isDebtorAgree(false)       //FIXME 작성자가 누군지에 따라 동의 여부 설정 필요
                .noteKey(null)              //FIXME
                .storageUrl(null)           //FIXME
                .build();

        promissoryPaperRepository.save(paper);
    }
}