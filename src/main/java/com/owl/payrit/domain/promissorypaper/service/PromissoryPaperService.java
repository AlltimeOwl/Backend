package com.owl.payrit.domain.promissorypaper.service;

import com.owl.payrit.domain.auth.dto.response.LoginUser;
import com.owl.payrit.domain.docsinfo.entity.DocsInfo;
import com.owl.payrit.domain.docsinfo.service.DocsInfoService;
import com.owl.payrit.domain.member.entity.CertificationInformation;
import com.owl.payrit.domain.member.entity.Member;
import com.owl.payrit.domain.member.service.MemberService;
import com.owl.payrit.domain.memo.dto.response.MemoListResponse;
import com.owl.payrit.domain.memo.entity.Memo;
import com.owl.payrit.domain.notification.entity.NotificationMessage;
import com.owl.payrit.domain.notification.event.NotificationEvent;
import com.owl.payrit.domain.promissorypaper.dto.request.PaperModifyRequest;
import com.owl.payrit.domain.promissorypaper.dto.request.PaperWriteRequest;
import com.owl.payrit.domain.promissorypaper.dto.response.PaperDetailResponse;
import com.owl.payrit.domain.promissorypaper.dto.response.PaperListResponse;
import com.owl.payrit.domain.promissorypaper.entity.PaperFormInfo;
import com.owl.payrit.domain.promissorypaper.entity.PaperProfile;
import com.owl.payrit.domain.promissorypaper.entity.PaperRole;
import com.owl.payrit.domain.promissorypaper.entity.PaperStatus;
import com.owl.payrit.domain.promissorypaper.entity.PromissoryPaper;
import com.owl.payrit.domain.promissorypaper.exception.PromissoryPaperErrorCode;
import com.owl.payrit.domain.promissorypaper.exception.PromissoryPaperException;
import com.owl.payrit.domain.promissorypaper.repository.PromissoryPaperRepository;
import com.owl.payrit.domain.repaymenthistory.dto.request.RepaymentCancelRequest;
import com.owl.payrit.domain.repaymenthistory.dto.request.RepaymentRequest;
import com.owl.payrit.domain.repaymenthistory.entity.RepaymentHistory;
import com.owl.payrit.domain.repaymenthistory.service.RepaymentHistoryService;
import com.owl.payrit.global.utils.Ut;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PromissoryPaperService {

    private static final String MODIFY_HEADER = "[MODIFY]";
    private static final Integer INTEREST_LIMIT = 20;
    private static final Long EXPIRED_STANDARD_DATE = 30L;

    private final DocsInfoService docsInfoService;
    private final RepaymentHistoryService repaymentHistoryService;
    private final MemberService memberService;
    private final PromissoryPaperRepository promissoryPaperRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public Long writePaper(LoginUser loginUser, PaperWriteRequest paperWriteRequest, HttpServletRequest req) throws IOException {

        Member loginedMember = memberService.findById(loginUser.id());

        Member creditor = memberService.findByPhoneNumberForPromissory(
                Ut.str.parsedPhoneNumber(paperWriteRequest.creditorPhoneNumber())).orElse(null);
        Member debtor = memberService.findByPhoneNumberForPromissory(
                Ut.str.parsedPhoneNumber(paperWriteRequest.debtorPhoneNumber())).orElse(null);

        DocsInfo docsInfo = docsInfoService.createByWriter(loginedMember, getIpByReq(req),
                loginedMember.getCertificationInformation().getImpUid());

        PromissoryPaper paper = PromissoryPaper.builder()
                .paperFormInfo(getFormInfoByReq(paperWriteRequest))
                .repaymentHistory(new ArrayList<>())
                .writer(loginedMember)
                .writerRole(paperWriteRequest.writerRole())
                .creditor(creditor)
                .creditorProfile(getProfileByReqAndRole(paperWriteRequest, PaperRole.CREDITOR))
                .isCreditorAgree(loginedMember.equals(creditor))
                .debtor(debtor)
                .debtorProfile(getProfileByReqAndRole(paperWriteRequest, PaperRole.DEBTOR))
                .isDebtorAgree(loginedMember.equals(debtor))
                .docsInfo(docsInfo)
                .memos(new ArrayList<>())
                .build();

        checkPaperData(loginedMember, paper);

        // 상대방도 가입 된 상태라면, 푸시 알림을 전송합니다
        Member notificationTarget = loginedMember.equals(creditor) ? debtor : creditor;
        if(notificationTarget != null) {
            String[] messageArgs = {notificationTarget.getName(), loginedMember.getCertificationInformation().getName()};
            NotificationEvent notificationEvent = new NotificationEvent(notificationTarget.getId(), NotificationMessage.APPROVAL_REQUEST, messageArgs);
            applicationEventPublisher.publishEvent(notificationEvent);
        }

        return promissoryPaperRepository.save(paper).getId();
    }

    public PaperDetailResponse getDetail(LoginUser loginUser, Long paperId) {

        PromissoryPaper paper = getById(paperId);
        Member loginedMember = memberService.findById(loginUser.id());

        List<MemoListResponse> memoListResponsesByPaper = getMemoListResponsesByPaper(paper, loginUser);

        PaperRole memberRole = isWriter(paper, loginedMember) ? paper.getWriterRole() : paper.getWriterRole().getReverse();

        if (!isMine(loginUser.id(), paper)) {
            throw new PromissoryPaperException(PromissoryPaperErrorCode.PAPER_IS_NOT_MINE);
        }

        return new PaperDetailResponse(paper, memberRole, calcRepaymentRate(paper), calcDueDate(paper),
                memoListResponsesByPaper);
    }

    public List<MemoListResponse> getMemoListResponsesByPaper(PromissoryPaper paper, LoginUser loginUser) {

        List<Memo> memos = paper.getMemos();

        return memos.stream()
                .filter(memo -> memo.getMemberId().equals(loginUser.id()))
                .map(MemoListResponse::new)
                .collect(Collectors.toList());
    }

    public boolean isWriter(PromissoryPaper paper, Member member) {

        return paper.getWriter() != null && paper.getWriter().equals(member);
    }

    public boolean isMine(Long memberId, PromissoryPaper promissoryPaper) {

        if (promissoryPaper.getCreditor() == null) {
            return promissoryPaper.getDebtor().getId().equals(memberId);
        }

        if (promissoryPaper.getDebtor() == null) {
            return promissoryPaper.getCreditor().getId().equals(memberId);
        }

        return promissoryPaper.getCreditor().getId().equals(memberId)
                || promissoryPaper.getDebtor().getId().equals(memberId);
    }

    public List<PaperListResponse> getAllListResponse(LoginUser loginUser) {

        List<PaperListResponse> creditorList =
                getListResponsesByRole(loginUser, PaperRole.CREDITOR);

        List<PaperListResponse> debtorList =
                getListResponsesByRole(loginUser, PaperRole.DEBTOR);

        List<PaperListResponse> paperListResponses = Stream.concat(creditorList.stream(), debtorList.stream())
                .collect(Collectors.toList());

        return paperListResponses;
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
                return new PaperListResponse(paper, PaperRole.CREDITOR, paper.getDebtorProfile().getName(),
                        calcDueDate(paper), calcRepaymentRate(paper), isWriter(paper, loginedMember));
            } else {
                return new PaperListResponse(paper, PaperRole.DEBTOR, paper.getCreditorProfile().getName()
                        , calcDueDate(paper), calcRepaymentRate(paper), isWriter(paper, loginedMember));
            }
        }).collect(Collectors.toList());
    }

    private long calcDueDate(PromissoryPaper paper) {

        LocalDate today = LocalDate.now();
        LocalDate repaymentEndDate = paper.getPaperFormInfo().getRepaymentEndDate();

        return ChronoUnit.DAYS.between(today, repaymentEndDate);
    }

    @Transactional
    public void acceptPaper(LoginUser loginUser, Long paperId, MultipartFile documentFile
            , HttpServletRequest req) throws IOException {

        Member loginedMember = memberService.findById(loginUser.id());
        PromissoryPaper paper = getById(paperId);
        DocsInfo docsInfo = paper.getDocsInfo();

        checkAcceptData(loginedMember, paper);

        docsInfoService.acceptByAccepter(docsInfo, loginedMember, getIpByReq(req),
                loginedMember.getCertificationInformation().getImpUid(), documentFile);

        paper.modifyPaperStatus(PaperStatus.PAYMENT_REQUIRED);

        // 차용증이 승인됐을 때, 양쪽 모두에게 알람을 전송합니다. (현재 둘다 앱 가입이 되어있다는 가정)
        String[] messageArgs = {paper.getCreditor().getCertificationInformation().getName(), paper.getDebtor().getCertificationInformation().getName()};
        NotificationEvent creditorNotificationEvent = new NotificationEvent(paper.getCreditor().getId(), NotificationMessage.PAYMENT_COMPLETE, messageArgs);
        NotificationEvent debtorNotificationEvent = new NotificationEvent(paper.getDebtor().getId(), NotificationMessage.PAYMENT_COMPLETE, messageArgs);

        applicationEventPublisher.publishEvent(creditorNotificationEvent);
        applicationEventPublisher.publishEvent(debtorNotificationEvent);
    }

    public void checkAcceptData(Member member, PromissoryPaper paper) {

        if (!paper.getPaperStatus().equals(PaperStatus.WAITING_AGREE)) {
            throw new PromissoryPaperException(PromissoryPaperErrorCode.PAPER_STATUS_IS_NOT_WAITING);
        }

        if (!checkMemberData(member, paper, paper.getWriterRole().getReverse())) {
            throw new PromissoryPaperException(PromissoryPaperErrorCode.PAPER_MATCHING_FAILED);
        }

        if (paper.getWriter().equals(member)) {
            throw new PromissoryPaperException(PromissoryPaperErrorCode.PAPER_CANNOT_ACCEPT_SELF);
        }
    }

    public PromissoryPaper getById(Long paperId) {

        return promissoryPaperRepository.findById(paperId).orElseThrow(
                () -> new PromissoryPaperException(PromissoryPaperErrorCode.PAPER_NOT_FOUND));
    }

    @Transactional
    public void sendModifyRequest(LoginUser loginUser, PaperModifyRequest paperModifyRequest) {

        Member loginedMember = memberService.findById(loginUser.id());
        PromissoryPaper paper = getById(paperModifyRequest.paperId());
        PaperFormInfo paperFormInfo = paper.getPaperFormInfo();

        checkModifyRequestData(loginedMember, paper);

        //TODO: 초기 작성자에게 paperModifyRequest의 contents로 알림(자체 or 알림톡) 발송하는 기능 필요
        Member writer = paper.getWriter();

        paper.modifyPaperStatus(PaperStatus.MODIFYING);
        paperFormInfo.addModifyMsg(MODIFY_HEADER + paperModifyRequest.contents());
    }

    public void checkModifyRequestData(Member member, PromissoryPaper paper) {

        if (!paper.getPaperStatus().equals(PaperStatus.WAITING_AGREE)) {
            throw new PromissoryPaperException(PromissoryPaperErrorCode.PAPER_STATUS_IS_NOT_WAITING);
        }

        if (!checkMemberData(member, paper, paper.getWriterRole().getReverse())) {
            throw new PromissoryPaperException(PromissoryPaperErrorCode.PAPER_CANNOT_REQUEST_MODIFY);
        }
    }

    @Transactional
    public void modifyingPaper(LoginUser loginUser, Long paperId, PaperWriteRequest paperWriteRequest) {

        Member loginedMember = memberService.findById(loginUser.id());
        PromissoryPaper paper = getById(paperId);

        checkPaperBeforeModify(loginedMember, paper);

        PromissoryPaper modifiedPaper = paper.toBuilder()
                .paperFormInfo(getFormInfoByReq(paperWriteRequest))
                .creditorProfile(getProfileByReqAndRole(paperWriteRequest, PaperRole.CREDITOR))
                .debtorProfile(getProfileByReqAndRole(paperWriteRequest, PaperRole.DEBTOR))
                .paperStatus(PaperStatus.WAITING_AGREE)
                .build();

        checkPaperData(loginedMember, modifiedPaper);

        promissoryPaperRepository.save(modifiedPaper);
    }

    public void checkPaperBeforeModify(Member member, PromissoryPaper paper) {

        if (!paper.getPaperStatus().equals(PaperStatus.MODIFYING)) {
            throw new PromissoryPaperException(PromissoryPaperErrorCode.PAPER_STATUS_IS_NOT_MODIFYING);
        }

        if (!paper.getWriter().equals(member)) {
            throw new PromissoryPaperException(PromissoryPaperErrorCode.PAPER_WRITER_CAN_MODIFY);
        }
    }

    public boolean checkMemberData(Member member, PromissoryPaper paper, PaperRole paperRole) {

        String name = member.getCertificationInformation().getName();
        String phoneNumber = member.getCertificationInformation().getPhone();

        if (paperRole.equals(PaperRole.CREDITOR)) {
            return name.equals(paper.getCreditorProfile().getName()) &&
                    phoneNumber.equals(paper.getCreditor().getCertificationInformation().getPhone());
        } else {
            return name.equals(paper.getDebtorProfile().getName()) &&
                    phoneNumber.equals(paper.getDebtor().getCertificationInformation().getPhone());
        }
    }

    public void checkPaperData(Member member, PromissoryPaper paper) {

        LocalDate today = LocalDate.now();

        PaperFormInfo paperFormInfo = paper.getPaperFormInfo();

        if (!member.isAuthenticated()) {
            throw new PromissoryPaperException(PromissoryPaperErrorCode.NEED_AUTHENTICATION);
        }

        if (!checkMemberData(member, paper, paper.getWriterRole())) {
            throw new PromissoryPaperException(PromissoryPaperErrorCode.PAPER_MATCHING_FAILED);
        }

        if (paperFormInfo.getInterestRate() > INTEREST_LIMIT) {
            throw new PromissoryPaperException(PromissoryPaperErrorCode.PAPER_INTEREST_RATE_NOT_VALID);
        }

        if (paperFormInfo.getRepaymentStartDate().isBefore(today)) {
            throw new PromissoryPaperException(PromissoryPaperErrorCode.PAPER_REPAYMENT_START_DATE_NOT_VALID);
        }

        if (paperFormInfo.getRepaymentEndDate().isBefore(paperFormInfo.getRepaymentStartDate())) {
            throw new PromissoryPaperException(PromissoryPaperErrorCode.PAPER_REPAYMENT_END_DATE_NOT_VALID);
        }
    }

    public double calcRepaymentRate(PromissoryPaper paper) {

        PaperFormInfo paperFormInfo = paper.getPaperFormInfo();

        long amount = paperFormInfo.getAmount();
        long needToRepaymentAmount = amount - paperFormInfo.getRemainingAmount();

        double repaymentRate = (double) needToRepaymentAmount / amount * 100.0;

        return Math.round(repaymentRate * 100.0) / 100.0;
    }

    //NOTE: 기간이 지나더라도 일부 상환, 메모 작성이 가능하도록 주석 처리
//    @Transactional
//    @Scheduled(cron = "0 0 0 * * ?")
//    public void expiringByRepaymentEndDate() {
//
//        List<PromissoryPaper> expiringTargets = promissoryPaperRepository
//                .findAllByRepaymentEndDateAndPaperStatus(LocalDate.now(), PaperStatus.COMPLETE_WRITING);
//
//        for (PromissoryPaper paper : expiringTargets) {
//            paper.modifyPaperStatus(PaperStatus.EXPIRED);
//        }
//    }

    @Transactional
    public void repayment(LoginUser loginUser, RepaymentRequest repaymentRequest) {

        PromissoryPaper paper = getById(repaymentRequest.paperId());
        Member loginedMember = memberService.findById(loginUser.id());
        PaperFormInfo paperFormInfo = paper.getPaperFormInfo();

        repaymentHistoryService.create(loginedMember, paper, repaymentRequest);

        long totalRemainingAmount = paperFormInfo.getRemainingAmount() - repaymentRequest.repaymentAmount();

        PromissoryPaper modifiedPaper = paper.toBuilder()
                .paperFormInfo(paperFormInfo.repayment(totalRemainingAmount))
                .build();

        if (totalRemainingAmount == 0) {
            modifiedPaper.modifyPaperStatus(PaperStatus.EXPIRED);
            // 빌려준 사람은 돈을 기록했으므로, null일수가 없음.
            Member creditor = paper.getCreditor();
            Optional<Member> debtor = Optional.ofNullable(paper.getDebtor());

            if(debtor.isPresent()) {
                String[] creditorNotificationArgs = { creditor.getCertificationInformation().getName(), debtor.get().getCertificationInformation().getName() };
                String[] debtorNotificationArgs = { debtor.get().getCertificationInformation().getName(), creditor.getCertificationInformation().getName() };

                NotificationEvent creditorNotificationEvent = new NotificationEvent(creditor.getId(), NotificationMessage.FULL_REPAYMENT_RECEIVED, creditorNotificationArgs);
                NotificationEvent debtorNotificationEvent = new NotificationEvent(debtor.get().getId(), NotificationMessage.FULL_REPAYMENT_MADE, debtorNotificationArgs);

                applicationEventPublisher.publishEvent(creditorNotificationEvent);
                applicationEventPublisher.publishEvent(debtorNotificationEvent);
            }else {
                // 상대방이 탈퇴한 경우라면, 암호화된 docsInfo에서 상대방 이름을 가져온 뒤, 푸시 알람을 전송합니다.
                DocsInfo docsInfo = paper.getDocsInfo();
                String writerName = docsInfo.getWriterName();
                String accepterName = docsInfo.getAccepterName();

                String creditorName = creditor.getCertificationInformation().getName();
                String debtorName = creditorName.equals(writerName)? accepterName : writerName;

                String[] creditorNotificationArgs = {creditorName, debtorName};
                NotificationEvent creditorNotificationEvent = new NotificationEvent(creditor.getId(), NotificationMessage.FULL_REPAYMENT_RECEIVED, creditorNotificationArgs);

                applicationEventPublisher.publishEvent(creditorNotificationEvent);
            }
        // 전액 상환이 아닐 시, 상환한 금액을 채무자에게 푸시알림 전송합니다.
        }else {
            Member creditor = paper.getCreditor();
            Optional<Member> debtor = Optional.ofNullable(paper.getDebtor());

            if(debtor.isPresent()) {
                String[] messageArgs = { creditor.getCertificationInformation().getName(), String.valueOf(repaymentRequest.repaymentAmount()) };
                NotificationEvent notificationEvent = new NotificationEvent(debtor.get().getId(), NotificationMessage.PARTIAL_REPAYMENT, messageArgs);
                applicationEventPublisher.publishEvent(notificationEvent);
            }
        }
        /*
        아래의 save부분은 사실 불필요합니다.
        hibernate의 변경감지 기능을 통해, 이 트랜잭션이 종료될 시 자동으로 update쿼리를 날리기 때문입니다.
        아래의 save 기능을 없앨 수 있다면, 위의 알람 전송 로직에서 early return으로 조금 더 가독성 있게 변경할 수 있습니다.
         */
        promissoryPaperRepository.save(modifiedPaper);
    }

    @Transactional
    public void cancelRepayment(LoginUser loginUser, RepaymentCancelRequest repaymentCancelRequest) {

        PromissoryPaper paper = getById(repaymentCancelRequest.paperId());
        Member loginedMember = memberService.findById(loginUser.id());
        PaperFormInfo paperFormInfo = paper.getPaperFormInfo();

        RepaymentHistory history = repaymentHistoryService.getById(repaymentCancelRequest.historyId());

        long needToCancelAmount = history.getRepaymentAmount();
        repaymentHistoryService.remove(loginedMember, paper, history);

        PromissoryPaper modifiedPaper = paper.toBuilder()
                .paperFormInfo(paperFormInfo.cancelRepayment(needToCancelAmount))
                .build();

        if(modifiedPaper.getPaperStatus().equals(PaperStatus.EXPIRED)) {
            modifiedPaper.modifyPaperStatus(PaperStatus.COMPLETE_WRITING);
        }

        Member creditor = paper.getCreditor();
        Optional<Member> debtor = Optional.ofNullable(paper.getDebtor());

        if(debtor.isPresent()) {
            String[] messageArgs = { creditor.getCertificationInformation().getName(), String.valueOf(history.getRepaymentAmount()) };
            NotificationEvent notificationEvent = new NotificationEvent(debtor.get().getId(), NotificationMessage.PARTIAL_REPAYMENT_CANCELLED, messageArgs);
            applicationEventPublisher.publishEvent(notificationEvent);
        }

        promissoryPaperRepository.save(modifiedPaper);
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void expiringForUnusedData() {

        //FIXME: 만료 기준 지정 및 보관
        LocalDateTime expiredStandardDate = LocalDateTime.now().minusDays(EXPIRED_STANDARD_DATE);

        List<PromissoryPaper> targetPapers = new ArrayList<>();

        List<PromissoryPaper> targetFromWaitingAgree = promissoryPaperRepository
                .findAllByUpdatedAtBeforeAndPaperStatus(expiredStandardDate, PaperStatus.WAITING_AGREE);

        List<PromissoryPaper> targetFromPaymentRequired = promissoryPaperRepository
                .findAllByUpdatedAtBeforeAndPaperStatus(expiredStandardDate, PaperStatus.PAYMENT_REQUIRED);

        targetPapers.addAll(targetFromWaitingAgree);
        targetPapers.addAll(targetFromPaymentRequired);

        promissoryPaperRepository.deleteAll(targetPapers);
    }

    public String getIpByReq(HttpServletRequest req) {

        String ip = req.getHeader("X-Forwarded-For");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = req.getHeader("Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = req.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = req.getHeader("HTTP_CLIENT_IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = req.getHeader("HTTP_X_FORWARDED_FOR");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = req.getHeader("X-Real-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = req.getHeader("X-RealIP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = req.getHeader("REMOTE_ADDR");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = req.getRemoteAddr();
        }

        return ip;
    }

    @Transactional
    public void removeRelation(Member member) {
//        promissoryPaperRepository.findAllByCreditorOrDebtorOrWriter(member).parallelStream()
//                .forEach(paper -> {
//                    if (paper.getWriter().equals(member)) paper.removeWriterRelation();
//                    if (paper.getDebtor().equals(member)) paper.removeDebtorRelation();
//                    if (paper.getCreditor().equals(member)) paper.removeCreditorRelation();
//                });

        List<PromissoryPaper> creditorList = promissoryPaperRepository.findAllByCreditor(member);
        List<PromissoryPaper> debtorList = promissoryPaperRepository.findAllByDebtor(member);
        List<PromissoryPaper> writerList = promissoryPaperRepository.findAllByWriter(member);

        creditorList.forEach(PromissoryPaper::removeCreditorRelation);
        debtorList.forEach(PromissoryPaper::removeDebtorRelation);
        writerList.forEach(PromissoryPaper::removeWriterRelation);

    }

    public PaperProfile getProfileByReqAndRole(PaperWriteRequest request, PaperRole paperRole) {

        String name = paperRole.equals(PaperRole.CREDITOR) ? request.creditorName() : request.debtorName();
        String phoneNumber = paperRole.equals(PaperRole.CREDITOR) ?
                Ut.str.parsedPhoneNumber(request.creditorPhoneNumber()) : Ut.str.parsedPhoneNumber(request.debtorPhoneNumber());
        String address = paperRole.equals(PaperRole.CREDITOR) ? request.creditorAddress() : request.debtorAddress();

        return new PaperProfile(name, phoneNumber, address);
    }

    public PaperFormInfo getFormInfoByReq(PaperWriteRequest paperWriteRequest) {

        return PaperFormInfo.builder()
                .primeAmount(paperWriteRequest.amount())
                .interest(paperWriteRequest.interest())
                .amount(paperWriteRequest.amount() + paperWriteRequest.interest())
                .remainingAmount(paperWriteRequest.amount() + paperWriteRequest.interest())
                .transactionDate(paperWriteRequest.transactionDate())
                .repaymentStartDate(paperWriteRequest.repaymentStartDate())
                .repaymentEndDate(paperWriteRequest.repaymentEndDate())
                .specialConditions(paperWriteRequest.specialConditions())
                .interestRate(paperWriteRequest.interestRate())
                .interestPaymentDate(paperWriteRequest.interestPaymentDate())
                .build();
    }

    @Transactional
    public void refuse(LoginUser loginUser, Long paperId) {

        Member loginedMember = memberService.findById(loginUser.id());
        PromissoryPaper paper = getById(paperId);

        checkRefuseConditions(loginedMember, paper);

        paper.modifyPaperStatus(PaperStatus.REFUSED);
    }

    public void checkRefuseConditions(Member loginedMember, PromissoryPaper paper) {

        PaperRole memberRole = isWriter(paper, loginedMember) ? paper.getWriterRole() : paper.getWriterRole().getReverse();
        Member memberInPaper = memberRole.equals(PaperRole.CREDITOR) ? paper.getCreditor() : paper.getDebtor();

        if (!paper.getPaperStatus().equals(PaperStatus.WAITING_AGREE)) {
            throw new PromissoryPaperException(PromissoryPaperErrorCode.REFUSE_NEED_WAITING_STATUS);
        }

        if (!memberInPaper.equals(loginedMember)) {
            throw new PromissoryPaperException(PromissoryPaperErrorCode.REFUSE_CANT_OTHER_PERSON);
        }
    }

    @Transactional
    public void reload(LoginUser loginUser) {

        Member loginedMember = memberService.findById(loginUser.id());
        CertificationInformation certInfo = loginedMember.getCertificationInformation();

        if(certInfo == null) {
            throw new PromissoryPaperException(PromissoryPaperErrorCode.PAPER_RELOAD_CAN_AFTER_CERTIFICATION);
        }

        String name = certInfo.getName();
        String phone = certInfo.getPhone();

        List<PromissoryPaper> targetsOfCreditor = getReloadTargets(name, phone, PaperRole.CREDITOR);
        for(PromissoryPaper target : targetsOfCreditor) {
            target.reloadCreditor(loginedMember);
        }

        List<PromissoryPaper> targetsOfDebtor = getReloadTargets(name, phone, PaperRole.DEBTOR);
        for(PromissoryPaper target : targetsOfDebtor) {
            target.reloadDebtor(loginedMember);
        }

        promissoryPaperRepository.saveAll(targetsOfCreditor);
        promissoryPaperRepository.saveAll(targetsOfDebtor);
    }

    public List<PromissoryPaper> getReloadTargets(String name, String phone, PaperRole paperRole) {

        List<PromissoryPaper> paperList;

        if (paperRole.equals(PaperRole.CREDITOR)) {
            paperList = promissoryPaperRepository.findAllByCreditorProfileNameAndCreditorProfilePhoneNumber(name, phone);
        } else {
            paperList = promissoryPaperRepository.findAllByDebtorProfileNameAndDebtorProfilePhoneNumber(name, phone);
        }

        return paperList.stream()
                .filter(paper -> paperRole.equals(PaperRole.CREDITOR) ? paper.getCreditor() == null : paper.getDebtor() == null)
                .filter(paper -> checkReloadConditions(paperRole.equals(PaperRole.CREDITOR) ? paper.getCreditorProfile() : paper.getDebtorProfile(), name, phone))
                .collect(Collectors.toList());
    }

    public boolean checkReloadConditions(PaperProfile profile, String name, String phone) {

        String paperProfileName = profile.getName();
        String paperProfilePhone = profile.getPhoneNumber();

        if (!name.equals(paperProfileName)) {
            log.info("{ 갱신 검사 결과 : 이름이 동일하지 않습니다.}");
            return false;
        }

        if (!phone.equals(Ut.str.parsedPhoneNumber(paperProfilePhone))) {
            log.info("{ 갱신 검사 결과 : 번호가 동일하지 않습니다.}");
            return false;
        }

        return true;
    }
}
