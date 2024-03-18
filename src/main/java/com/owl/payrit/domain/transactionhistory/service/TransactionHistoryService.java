package com.owl.payrit.domain.transactionhistory.service;

import com.owl.payrit.domain.auth.dto.response.LoginUser;
import com.owl.payrit.domain.member.entity.Member;
import com.owl.payrit.domain.member.service.MemberService;
import com.owl.payrit.domain.transactionhistory.dto.request.TransactionHistorySaveRequest;
import com.owl.payrit.domain.transactionhistory.entity.TransactionHistory;
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

    private final TransactionHistoryRepository transactionHistoryRepository;

    @Transactional
    public void saveHistory(LoginUser loginUser, TransactionHistorySaveRequest request) {

        Member loginedMember = memberService.findById(loginUser.id());

        TransactionHistory history = TransactionHistory.builder()
                .paidMember(loginedMember)
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

}
