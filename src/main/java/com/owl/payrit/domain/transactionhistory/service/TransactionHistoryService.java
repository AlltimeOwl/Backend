package com.owl.payrit.domain.transactionhistory.service;

import com.owl.payrit.domain.auth.dto.response.LoginUser;
import com.owl.payrit.domain.member.entity.Member;
import com.owl.payrit.domain.member.service.MemberService;
import com.owl.payrit.domain.promissorypaper.entity.PromissoryPaper;
import com.owl.payrit.domain.promissorypaper.service.PromissoryPaperService;
import com.owl.payrit.domain.transactionhistory.dto.request.TransactionHistorySaveRequest;
import com.owl.payrit.domain.transactionhistory.dto.response.TransactionHistoryDetailResponse;
import com.owl.payrit.domain.transactionhistory.entity.TransactionHistory;
import com.owl.payrit.domain.transactionhistory.exception.TransactionHistoryErrorCode;
import com.owl.payrit.domain.transactionhistory.exception.TransactionHistoryException;
import com.owl.payrit.domain.transactionhistory.repository.TransactionHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionHistoryService {

    private final MemberService memberService;
    private final PromissoryPaperService promissoryPaperService;

    private final TransactionHistoryRepository transactionHistoryRepository;

    @Transactional
    public void saveHistory(LoginUser loginUser, TransactionHistorySaveRequest request) {

        PromissoryPaper paper = promissoryPaperService.getById(request.paperId());
        Member loginedMember = memberService.findById(loginUser.id());
        
        //TODO: 결제 가능 여부 체크 필요

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

        //TODO: 조회 조건 체크 필요
        
        TransactionHistory history = getById(historyId);
        
        return new TransactionHistoryDetailResponse(history);
    }

    public TransactionHistory getById(Long historyId) {

        return transactionHistoryRepository.findById(historyId).orElseThrow(
                () -> new TransactionHistoryException(TransactionHistoryErrorCode.HISTORY_NOT_FOUND));
    }

}
