package com.owl.payrit.domain.transactionhistory.service;

import com.owl.payrit.domain.auth.dto.response.LoginUser;
import com.owl.payrit.domain.auth.dto.response.PortOneTokenResponse;
import com.owl.payrit.domain.auth.provider.portone.PortOneApiClient;
import com.owl.payrit.domain.auth.service.AuthService;
import com.owl.payrit.domain.member.entity.Member;
import com.owl.payrit.domain.member.service.MemberService;
import com.owl.payrit.domain.promissorypaper.entity.PaperStatus;
import com.owl.payrit.domain.promissorypaper.entity.PromissoryPaper;
import com.owl.payrit.domain.promissorypaper.service.PromissoryPaperService;
import com.owl.payrit.domain.transactionhistory.configuration.PaymentConfigProps;
import com.owl.payrit.domain.transactionhistory.dto.request.TransactionHistorySaveRequest;
import com.owl.payrit.domain.transactionhistory.dto.response.PaymentInfoResponse;
import com.owl.payrit.domain.transactionhistory.dto.response.TransactionHistoryDetailResponse;
import com.owl.payrit.domain.transactionhistory.dto.response.TransactionHistoryListResponse;
import com.owl.payrit.domain.transactionhistory.dto.response.PortOnePaymentInfoResponse;
import com.owl.payrit.domain.transactionhistory.entity.TransactionHistory;
import com.owl.payrit.domain.transactionhistory.entity.TransactionType;
import com.owl.payrit.domain.transactionhistory.exception.TransactionHistoryErrorCode;
import com.owl.payrit.domain.transactionhistory.exception.TransactionHistoryException;
import com.owl.payrit.domain.transactionhistory.repository.TransactionHistoryRepository;
import com.owl.payrit.global.utils.Ut;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionHistoryService {

    private final MemberService memberService;
    private final PaymentConfigProps paymentConfigProps;
    private final PromissoryPaperService promissoryPaperService;
    private final AuthService authService;
    private final PortOneApiClient portOneApiClient;

    private final TransactionHistoryRepository transactionHistoryRepository;

    public PaymentInfoResponse getPaymentInfo(Long memberId, Long paperId, TransactionType transactionType) {

        Member loginedMember = memberService.findById(memberId);

        String PID = paymentConfigProps.getPID();
        String PGCode = paymentConfigProps.getTestPGCode();     //FIXME: PGCODE <-> TESTPGCODE
        String merchantUID = genMerchantUID(paperId);
        String name = transactionType.getContent();
        int amount = getCostByType(transactionType);
        String buyerEmail = loginedMember.getEmail();
        String buyerName = loginedMember.getName();
        String buyerTel = loginedMember.getPhoneNumber();

        PromissoryPaper targetPaper = promissoryPaperService.getById(paperId);

        if (!targetPaper.getPaperStatus().equals(PaperStatus.PAYMENT_REQUIRED)) {
            throw new TransactionHistoryException(TransactionHistoryErrorCode.TRANSACTION_CANT_BEFORE_ACCEPT);
        }

        return new PaymentInfoResponse(PID, PGCode, merchantUID, name, amount, buyerEmail, buyerName, buyerTel);
    }

    @Transactional
    public void saveHistory(LoginUser loginUser, TransactionHistorySaveRequest request) {

        PromissoryPaper paper = promissoryPaperService.getById(request.paperId());
        Member loginedMember = memberService.findById(loginUser.id());

        PortOneTokenResponse portOneTokenResponse = authService.getPortOneAccessToken();

        PortOnePaymentInfoResponse portOnePaymentInfoRes =
                portOneApiClient.getSinglePaymentInformation("Bearer %s".formatted(portOneTokenResponse.response().accessToken()), request.impUid());

        TransactionHistory history = TransactionHistory.builder()
                .paidMember(loginedMember)
                .linkedPaper(paper)
                .transactionDate(request.transactionDate())
                .amount(request.amount())
                .contents(request.transactionType().getContent())
                .paymentMethod(getPaymentMethodByPortOneRes(portOnePaymentInfoRes))
                .applyNum(portOnePaymentInfoRes.response().applyNum())
                .impUid(request.impUid())
                .merchantUid(request.merchantUid())
                .isSuccess(request.isSuccess())
                .build();

        checkSaveRequest(loginedMember, paper, history);

        paper.modifyPaperStatus(PaperStatus.COMPLETE_WRITING);
        paper.paid();

        transactionHistoryRepository.save(history);
    }


    public TransactionHistoryDetailResponse getDetail(LoginUser loginUser, Long historyId) {

        Member loginedMember = memberService.findById(loginUser.id());

        TransactionHistory history = getById(historyId);

        checkDetailRequest(loginedMember, history);

        return new TransactionHistoryDetailResponse(history);
    }

    public TransactionHistory getById(Long historyId) {

        return transactionHistoryRepository.findById(historyId).orElseThrow(
                () -> new TransactionHistoryException(TransactionHistoryErrorCode.TRANSACTION_NOT_FOUND));
    }

    public List<TransactionHistoryListResponse> getListResponses(LoginUser loginUser) {

        Member loginedMember = memberService.findById(loginUser.id());

        List<TransactionHistory> histories = getAllByPaidMember(loginedMember);

        List<TransactionHistoryListResponse> historyListResponses = new ArrayList<>();

        for (TransactionHistory history : histories) {

            LocalDate transactionDate = history.getTransactionDate().toLocalDate();

            historyListResponses.add(new TransactionHistoryListResponse(history, transactionDate));
        }

        return historyListResponses;
    }

    public List<TransactionHistory> getAllByPaidMember(Member member) {

        return transactionHistoryRepository.findAllByPaidMember(member);
    }

    public void checkSaveRequest(Member loginedMember, PromissoryPaper paper, TransactionHistory transactionHistory) {

        if (transactionHistory.getAmount() != paymentConfigProps.getPaperCost()) {        //FIXME: 수수료 부과시 변경 필요
            throw new TransactionHistoryException(TransactionHistoryErrorCode.TRANSACTION_BAD_COST);
        }

        if (transactionHistory.getTransactionDate().isAfter(LocalDateTime.now())) {
            throw new TransactionHistoryException(TransactionHistoryErrorCode.TRANSACTION_BAD_DATE);
        }

        if (!paper.getWriter().equals(loginedMember)) {
            throw new TransactionHistoryException(TransactionHistoryErrorCode.TRANSACTION_ONLY_WRITER);
        }

        if (transactionHistoryRepository.findByMerchantUid(transactionHistory.getMerchantUid()).isPresent()) {
            throw new TransactionHistoryException(TransactionHistoryErrorCode.TRANSACTION_MERCHANT_UID_CONFLICT);
        }

        if (transactionHistoryRepository.findByApplyNum(transactionHistory.getApplyNum()).isPresent()) {
            throw new TransactionHistoryException(TransactionHistoryErrorCode.TRANSACTION_APPROVAL_NUM_CONFLICT);
        }

        if (transactionHistoryRepository.findByImpUid(transactionHistory.getImpUid()).isPresent()) {
            throw new TransactionHistoryException(TransactionHistoryErrorCode.TRANSACTION_MERCHANT_UID_CONFLICT);
        }
    }

    public void checkDetailRequest(Member loginedMember, TransactionHistory history) {

        if (!history.getPaidMember().equals(loginedMember)) {
            throw new TransactionHistoryException(TransactionHistoryErrorCode.TRANSACTION_FORBIDDEN);
        }

        //TODO ...
    }

    public String genMerchantUID(Long paperId) {

        Random random = new Random();
        LocalDateTime now = LocalDateTime.now();

        int randomValue = random.nextInt(9) + 1;

        return "PR_" + paperId + now.getMonthValue() + now.getDayOfMonth() + now.getSecond() + randomValue;
    }

    public int getCostByType(TransactionType transactionType) {

        switch (transactionType) {
            case PAPER_TRANSACTION:
                return paymentConfigProps.getPaperCost();
            case NOTIFICATION_TRANSACTION:
                return paymentConfigProps.getNotiCost();
            default:
                throw new TransactionHistoryException(TransactionHistoryErrorCode.PAYMENT_BAD_TYPE);
        }
    }

    public String getPaymentMethodByPortOneRes(PortOnePaymentInfoResponse portOneResponse) {

        StringBuilder sb = new StringBuilder();

        sb.append(portOneResponse.response().cardName());
        sb.append(Ut.str.getCardNumberPrefix(portOneResponse.response().cardNumber()));

        return sb.toString();
    }
}
