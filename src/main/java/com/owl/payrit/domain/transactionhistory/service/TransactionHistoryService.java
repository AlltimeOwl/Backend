package com.owl.payrit.domain.transactionhistory.service;

import com.owl.payrit.domain.auth.dto.response.LoginUser;
import com.owl.payrit.domain.auth.dto.response.PortOneTokenResponse;
import com.owl.payrit.domain.auth.provider.portone.PortOneApiClient;
import com.owl.payrit.domain.auth.service.AuthService;
import com.owl.payrit.domain.member.entity.CertificationInformation;
import com.owl.payrit.domain.member.entity.Member;
import com.owl.payrit.domain.member.service.MemberService;
import com.owl.payrit.domain.promissorypaper.entity.PaperStatus;
import com.owl.payrit.domain.promissorypaper.entity.PromissoryPaper;
import com.owl.payrit.domain.promissorypaper.service.PromissoryPaperService;
import com.owl.payrit.domain.transactionhistory.configuration.PaymentConfigProps;
import com.owl.payrit.domain.transactionhistory.dto.request.PortOnePaymentCancelRequest;
import com.owl.payrit.domain.transactionhistory.dto.request.TransactionHistorySaveRequest;
import com.owl.payrit.domain.transactionhistory.dto.response.*;
import com.owl.payrit.domain.transactionhistory.entity.TransactionHistory;
import com.owl.payrit.domain.transactionhistory.entity.TransactionType;
import com.owl.payrit.domain.transactionhistory.exception.TransactionHistoryErrorCode;
import com.owl.payrit.domain.transactionhistory.exception.TransactionHistoryException;
import com.owl.payrit.domain.transactionhistory.repository.TransactionHistoryRepository;
import com.owl.payrit.global.utils.Ut;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

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
        PromissoryPaper targetPaper = promissoryPaperService.getById(paperId);

        //TODO: conditions 묶어서 정리
        if (!targetPaper.getPaperStatus().equals(PaperStatus.PAYMENT_REQUIRED)) {
            throw new TransactionHistoryException(TransactionHistoryErrorCode.TRANSACTION_CANT_BEFORE_ACCEPT);
        }

        if(!loginedMember.isAuthenticated()) {
            throw new TransactionHistoryException(TransactionHistoryErrorCode.TRANSACTION_NEED_AUTHENTICATION);
        }

        String PID = paymentConfigProps.getPID();
        String PGCode = paymentConfigProps.getPGCode();     //NOTE: PGCODE <-> TESTPGCODE
        String merchantUID = genMerchantUID(paperId);
        String name = transactionType.getContent();
        int amount = getCostByType(transactionType);
        String buyerEmail = loginedMember.getEmail();
        String buyerName = loginedMember.getCertificationInformation().getName();
        String buyerTel = loginedMember.getCertificationInformation().getPhone();

        return new PaymentInfoResponse(PID, PGCode, merchantUID, name, amount, buyerEmail, buyerName, buyerTel);
    }

    @Transactional
    public void saveHistory(LoginUser loginUser, TransactionHistorySaveRequest request) {

        PromissoryPaper paper = promissoryPaperService.getById(request.paperId());
        Member loginedMember = memberService.findById(loginUser.id());
        CertificationInformation certInfo = loginedMember.getCertificationInformation();

        PortOneTokenResponse portOneTokenResponse = authService.getPortOneAccessToken();

        PortOnePaymentInfoResponse portOnePaymentInfoRes =
                portOneApiClient.getSinglePaymentInformation("Bearer %s".formatted(portOneTokenResponse.response().accessToken()), request.impUid());

        TransactionHistory history = TransactionHistory.builder()
                .buyerName(certInfo.getName())
                .buyerPhone(certInfo.getPhone())
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

        CertificationInformation certInfo = member.getCertificationInformation();

        return transactionHistoryRepository.findAllByBuyerNameAndBuyerPhone(certInfo.getName(), certInfo.getPhone());
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

        CertificationInformation certInfo = loginedMember.getCertificationInformation();

        if (!certInfo.getPhone().equals(history.getBuyerPhone())) {
            throw new TransactionHistoryException(TransactionHistoryErrorCode.TRANSACTION_FORBIDDEN);
        }

        if (!certInfo.getName().equals(history.getBuyerName())) {
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

    public PortOnePaymentCancelResponse cancelForDev(LoginUser loginUser, String secretKey, PortOnePaymentCancelRequest request) {

        if (!secretKey.equals(paymentConfigProps.getSecretKeyForDev())) {
            throw new TransactionHistoryException(TransactionHistoryErrorCode.SECRET_KEY_NOT_VALID);
        }

        PortOneTokenResponse portOneTokenResponse = authService.getPortOneAccessToken();

        RestTemplate restTemplate = new RestTemplateBuilder().build();
        try {
            JSONObject formData = new JSONObject();
            formData.put("imp_uid", request.impUid());
            formData.put("merchant_uid", request.merchantUid());
            formData.put("reason", request.reason());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Authorization", "Bearer %s".formatted(portOneTokenResponse.response().accessToken()));

            HttpEntity<String> requestEntity = new HttpEntity<>(formData.toString(), headers);

            ResponseEntity<PortOnePaymentCancelResponse> responseEntity =
                    restTemplate.postForEntity("https://api.iamport.kr/payments/cancel", requestEntity, PortOnePaymentCancelResponse.class);

            return responseEntity.getBody();
        } catch (Exception e) {
            log.error("Error occurred while canceled payment", e);
        }
        return null;
    }
}
