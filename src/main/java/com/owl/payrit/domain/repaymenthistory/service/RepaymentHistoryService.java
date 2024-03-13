package com.owl.payrit.domain.repaymenthistory.service;

import com.owl.payrit.domain.promissorypaper.entity.PromissoryPaper;
import com.owl.payrit.domain.promissorypaper.service.PromissoryPaperService;
import com.owl.payrit.domain.repaymenthistory.dto.request.RepaymentRequest;
import com.owl.payrit.domain.repaymenthistory.entity.RepaymentHistory;
import com.owl.payrit.domain.repaymenthistory.repository.RepaymentHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RepaymentHistoryService {

    private final RepaymentHistoryRepository repaymentHistoryRepository;

    @Transactional
    public void create(PromissoryPaper paper, RepaymentRequest repaymentRequest) {

        RepaymentHistory repaymentHistory = RepaymentHistory.builder()
                .paper(paper)
                .repaymentAmount(repaymentRequest.repaymentAmount())
                .repaymentDate(repaymentRequest.repaymentDate())
                .build();

        repaymentHistoryRepository.save(repaymentHistory);
    }
}
