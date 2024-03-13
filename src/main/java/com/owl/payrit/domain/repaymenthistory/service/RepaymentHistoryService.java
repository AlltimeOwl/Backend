package com.owl.payrit.domain.repaymenthistory.service;

import com.owl.payrit.domain.member.entity.Member;
import com.owl.payrit.domain.promissorypaper.entity.PaperStatus;
import com.owl.payrit.domain.promissorypaper.entity.PromissoryPaper;
import com.owl.payrit.domain.promissorypaper.exception.PromissoryPaperException;
import com.owl.payrit.domain.promissorypaper.service.PromissoryPaperService;
import com.owl.payrit.domain.repaymenthistory.dto.request.RepaymentRequest;
import com.owl.payrit.domain.repaymenthistory.entity.RepaymentHistory;
import com.owl.payrit.domain.repaymenthistory.repository.RepaymentHistoryRepository;
import com.owl.payrit.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RepaymentHistoryService {

    private final RepaymentHistoryRepository repaymentHistoryRepository;

    @Transactional
    public void create(Member member, PromissoryPaper paper, RepaymentRequest repaymentRequest) {

        checkRepaymentConditions(member, paper, repaymentRequest);

        RepaymentHistory repaymentHistory = RepaymentHistory.builder()
                .paper(paper)
                .repaymentAmount(repaymentRequest.repaymentAmount())
                .repaymentDate(repaymentRequest.repaymentDate())
                .build();

        repaymentHistoryRepository.save(repaymentHistory);
    }

    public void checkRepaymentConditions(Member member, PromissoryPaper paper, RepaymentRequest repaymentRequest) {

        LocalDate requestDate = repaymentRequest.repaymentDate();
        LocalDate repaymentStartDate = paper.getRepaymentStartDate();
        LocalDate repaymentEndDate = paper.getRepaymentEndDate();

        if(!paper.getPaperStatus().equals(PaperStatus.COMPLETE_WRITING)){
            throw new PromissoryPaperException(ErrorCode.REPAYMENT_STATUS_ERROR);
        }

        if(!paper.getCreditor().equals(member)) {
            throw new PromissoryPaperException(ErrorCode.REPAYMENT_ONLY_ACCESS_CREDITOR);
        }

        if(requestDate.isBefore(repaymentStartDate) || requestDate.isAfter(repaymentEndDate)) {
            throw new PromissoryPaperException(ErrorCode.REPAYMENT_NOT_VALID_DATE);
        }

        if(paper.getRemainingAmount() < repaymentRequest.repaymentAmount()) {
            throw new PromissoryPaperException(ErrorCode.REPAYMENT_AMOUNT_OVER);
        }
    }
}
