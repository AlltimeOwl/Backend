package com.owl.payrit.domain.promissorypaper.service;

import com.owl.payrit.domain.auth.dto.response.LoginUser;
import com.owl.payrit.domain.member.entity.Member;
import com.owl.payrit.domain.member.service.MemberService;
import com.owl.payrit.domain.promissorypaper.dto.request.PaperModifyRequest;
import com.owl.payrit.domain.promissorypaper.dto.request.PaperWriteRequest;
import com.owl.payrit.domain.promissorypaper.dto.response.PaperDetailResponse;
import com.owl.payrit.domain.promissorypaper.dto.response.PaperListResponse;
import com.owl.payrit.domain.promissorypaper.entity.PaperRole;
import com.owl.payrit.domain.promissorypaper.entity.PaperStatus;
import com.owl.payrit.domain.promissorypaper.entity.PromissoryPaper;
import com.owl.payrit.domain.promissorypaper.exception.PromissoryPaperException;
import com.owl.payrit.domain.promissorypaper.repository.PromissoryPaperRepository;
import com.owl.payrit.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        //TODO: 본인 인증이 완료된 회원만 차용증 작성이 가능하다.(v2)

        //TODO: 이자율은 음수가 될 수 없다.

        //TODO: 상환 시작일(송금일)은 당일 이후여야 하며, 상환 마감일은 상환 시작일 이후여야 한다.

        PromissoryPaper paper = PromissoryPaper.builder()
                .amount(paperWriteRequest.amount())
                .transactionDate(paperWriteRequest.transactionDate())
                .repaymentStartDate(paperWriteRequest.repaymentStartDate())
                .repaymentEndDate(paperWriteRequest.repaymentEndDate())
                .specialConditions(paperWriteRequest.specialConditions())
                .interestRate(paperWriteRequest.interestRate())
                .writer(loginedMember)
                .writerRole(paperWriteRequest.writerRole())
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

        //REVIEW: 승인시에도 즉시 확인이 가능하도록 엔티티에 작성자 역할 추가, 검증 순서를 아래로 내림(고려)
        if(!checkMemberData(loginedMember, paper, paper.getWriterRole())) {
            throw new PromissoryPaperException(ErrorCode.PAPER_MATCHING_FAILED);
        }

        promissoryPaperRepository.save(paper);
    }

    private String getRandomKey() {
        String paperKey = UUID.randomUUID().toString();
        return promissoryPaperRepository.existsByPaperKey(paperKey) ? getRandomKey() : paperKey;
    }

    public PaperDetailResponse getDetail(LoginUser loginUser, Long paperId) {

        PromissoryPaper promissoryPaper = getById(paperId);

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

    public List<PaperListResponse> getAllListResponse(LoginUser loginUser) {

        List<PaperListResponse> creditorList =
                getListResponsesByRole(loginUser, PaperRole.CREDITOR);

        List<PaperListResponse> debtorList =
                getListResponsesByRole(loginUser, PaperRole.DEBTOR);

        return Stream.concat(creditorList.stream(), debtorList.stream())
                .collect(Collectors.toList());
    }

    public List<PaperListResponse> getListResponsesByRole(LoginUser loginUser, PaperRole role) {

        Member loginedMember = memberService.findById(loginUser.id());

        List<PromissoryPaper> papers;

        if (role.equals(PaperRole.CREDITOR)) {
            papers = promissoryPaperRepository.findAllByCreditor(loginedMember);
        } else {
            papers = promissoryPaperRepository.findAllByDebtor(loginedMember);
        }

        return papers.stream().map(paper -> {
            if (role.equals(PaperRole.CREDITOR)) {
                return new PaperListResponse(paper, PaperRole.CREDITOR, paper.getDebtorName(), calcDueDate(paper));
            } else {
                return new PaperListResponse(paper, PaperRole.DEBTOR, paper.getCreditorName(), calcDueDate(paper));
            }
        }).collect(Collectors.toList());
    }

    private long calcDueDate(PromissoryPaper paper) {

        LocalDate today = LocalDate.now();
        LocalDate repaymentEndDate = paper.getRepaymentEndDate();

        return ChronoUnit.DAYS.between(today, repaymentEndDate);
    }

    @Transactional
    public void acceptPaper(LoginUser loginUser, Long paperId) {

        Member loginedMember = memberService.findById(loginUser.id());
        PromissoryPaper paper = getById(paperId);

        //승인 대기 단계에서만 승인이 가능함
        if (!paper.getPaperStatus().equals(PaperStatus.WAITING_AGREE)) {
            throw new PromissoryPaperException(ErrorCode.PAPER_STATUS_NOT_VALID);
        }

        //할당 역할에 대한 정보와 회원 정보가 일치해야함
        if (!checkMemberData(loginedMember, paper, paper.getWriterRole().getReverse())) {
            throw new PromissoryPaperException(ErrorCode.PAPER_MATCHING_FAILED);
        }

        //작성자와 승인자가 일치할 수 없음
        if (paper.getWriter().equals(loginedMember)) {
            throw new PromissoryPaperException(ErrorCode.PAPER_CANNOT_ACCEPT_SELF);
        }

        paper.modifyPaperStatus(PaperStatus.PAYMENT_REQUIRED);
    }

    public PromissoryPaper getById(Long paperId) {

        return promissoryPaperRepository.findById(paperId).orElseThrow(
                () -> new PromissoryPaperException(ErrorCode.PAPER_NOT_FOUND));
    }

    @Transactional
    public void sendModifyRequest(LoginUser loginUser, PaperModifyRequest paperModifyRequest) {

        Member loginedMember = memberService.findById(loginUser.id());
        PromissoryPaper paper = getById(paperModifyRequest.paperId());

        //승인 대기 단계에서만 수정 요청이 가능함
        if (!paper.getPaperStatus().equals(PaperStatus.WAITING_AGREE)) {
            throw new PromissoryPaperException(ErrorCode.PAPER_STATUS_NOT_VALID);
        }

        //승인 요청을 받은 사람만 수정 요청이 가능하다.
        if (!checkMemberData(loginedMember, paper, paper.getWriterRole().getReverse())) {
            throw new PromissoryPaperException(ErrorCode.PAPER_IS_NOT_MINE);
        }

        //TODO: 초기 작성자에게 paperModifyRequest의 contents로 알림(자체 or 알림톡) 발송하는 기능 필요
        Member writer = memberService.findById(paperModifyRequest.writerId());

        paper.modifyPaperStatus(PaperStatus.MODIFYING);
    }

    @Transactional
    public void modifyingPaper(LoginUser loginUser, Long paperId, PaperWriteRequest paperWriteRequest) {

        Member loginedMember = memberService.findById(loginUser.id());
        PromissoryPaper paper = getById(paperId);

        if(!paper.getPaperStatus().equals(PaperStatus.MODIFYING)) {
            throw new PromissoryPaperException(ErrorCode.PAPER_STATUS_NOT_VALID);
        }

        if (!paper.getWriter().equals(loginedMember)) {
            throw new PromissoryPaperException(ErrorCode.PAPER_WRITER_CAN_MODIFY);
        }

        //TODO: 더 좋은 방법 고려 필요. 수정할 부분이 어딘지 명시된다면?
        PromissoryPaper modifiedPaper = paper.toBuilder()
                .amount(paperWriteRequest.amount())
                .transactionDate(paperWriteRequest.transactionDate())
                .repaymentStartDate(paperWriteRequest.repaymentStartDate())
                .repaymentEndDate(paperWriteRequest.repaymentEndDate())
                .specialConditions(paperWriteRequest.specialConditions())
                .interestRate(paperWriteRequest.interestRate())
                .creditorName(paperWriteRequest.creditorName())
                .creditorPhoneNumber(paperWriteRequest.creditorPhoneNumber())
                .creditorAddress(paperWriteRequest.creditorAddress())
                .debtorName(paperWriteRequest.debtorName())
                .debtorPhoneNumber(paperWriteRequest.debtorPhoneNumber())
                .debtorAddress(paperWriteRequest.debtorAddress())
                .paperStatus(PaperStatus.WAITING_AGREE)
                .build();

        promissoryPaperRepository.save(modifiedPaper);
    }

    public boolean checkMemberData(Member member, PromissoryPaper paper, PaperRole paperRole) {

        String name = member.getName();
        String phoneNumber = member.getPhoneNumber();

        if(paperRole.equals(PaperRole.CREDITOR)) {
            return name.equals(paper.getCreditorName()) &&
                    phoneNumber.equals(paper.getCreditorPhoneNumber());
        } else {
            return name.equals(paper.getDebtorName()) &&
                    phoneNumber.equals(paper.getDebtorPhoneNumber());
        }
    }
}