package com.owl.payrit.domain.transactionhistory.service;

import com.owl.payrit.domain.auth.dto.response.LoginUser;
import com.owl.payrit.domain.member.entity.Member;
import com.owl.payrit.domain.member.service.MemberService;
import com.owl.payrit.domain.promissorypaper.dto.response.PaperListResponse;
import com.owl.payrit.domain.promissorypaper.entity.PaperRole;
import com.owl.payrit.domain.promissorypaper.entity.PromissoryPaper;
import com.owl.payrit.domain.promissorypaper.service.PromissoryPaperService;
import com.owl.payrit.domain.transactionhistory.dto.request.TransactionHistorySaveRequest;
import com.owl.payrit.domain.transactionhistory.dto.response.TransactionHistoryDetailResponse;
import com.owl.payrit.domain.transactionhistory.dto.response.TransactionHistoryListResponse;
import com.owl.payrit.domain.transactionhistory.entity.TransactionHistory;
import com.owl.payrit.domain.transactionhistory.exception.TransactionHistoryErrorCode;
import com.owl.payrit.domain.transactionhistory.exception.TransactionHistoryException;
import com.owl.payrit.domain.transactionhistory.repository.TransactionHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionHistoryService {

    private final MemberService memberService;
    private final PromissoryPaperService promissoryPaperService;

    private final TransactionHistoryRepository transactionHistoryRepository;

    //FIXME: 가격 설정 하드코딩 배제 필요
    private static final long PAPER_COST = 1000;

    @Transactional
    public void saveHistory(LoginUser loginUser, TransactionHistorySaveRequest request) {

        PromissoryPaper paper = promissoryPaperService.getById(request.paperId());
        Member loginedMember = memberService.findById(loginUser.id());

        checkSaveRequest(loginedMember, paper, request);

        TransactionHistory history = TransactionHistory.builder()
                .paidMember(loginedMember)
                .linkedPaper(paper)
                .transactionDate(request.transactionDate())
                .amount(request.amount())
                .contents(request.contents())
                .transactionType(request.transactionType())
                .approvalNumber(request.approvalNumber())
                .orderNumber(request.orderNumber())
                .isSuccess(request.isSuccess())
                .build();

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

    public void checkSaveRequest(Member loginedMember, PromissoryPaper paper, TransactionHistorySaveRequest request) {

        if (request.amount() != PAPER_COST) {
            throw new TransactionHistoryException(TransactionHistoryErrorCode.TRANSACTION_BAD_COST);
        }

        if (request.transactionDate().isAfter(LocalDateTime.now())) {
            throw new TransactionHistoryException(TransactionHistoryErrorCode.TRANSACTION_BAD_DATE);
        }

        if (!paper.getWriter().equals(loginedMember)) {
            throw new TransactionHistoryException(TransactionHistoryErrorCode.TRANSACTION_ONLY_WRITER);
        }

        if (transactionHistoryRepository.findByOrderNumber(request.orderNumber()).isPresent()) {
            throw new TransactionHistoryException(TransactionHistoryErrorCode.TRANSACTION_ORDER_NUM_CONFLICT);
        }

        if (transactionHistoryRepository.findByApprovalNumber(request.approvalNumber()).isPresent()) {
            throw new TransactionHistoryException(TransactionHistoryErrorCode.TRANSACTION_APPROVAL_NUM_CONFLICT);
        }
    }

    public void checkDetailRequest(Member loginedMember, TransactionHistory history) {

        if (!history.getPaidMember().equals(loginedMember)) {
            throw new TransactionHistoryException(TransactionHistoryErrorCode.TRANSACTION_FORBIDDEN);
        }

        //TODO ...
    }

}
