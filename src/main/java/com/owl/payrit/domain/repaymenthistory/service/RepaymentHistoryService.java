package com.owl.payrit.domain.repaymenthistory.service;

import com.owl.payrit.domain.member.entity.Member;
import com.owl.payrit.domain.promissorypaper.entity.PaperStatus;
import com.owl.payrit.domain.promissorypaper.entity.PromissoryPaper;
import com.owl.payrit.domain.repaymenthistory.dto.request.RepaymentRequest;
import com.owl.payrit.domain.repaymenthistory.entity.RepaymentHistory;
import com.owl.payrit.domain.repaymenthistory.exception.RepaymentErrorCode;
import com.owl.payrit.domain.repaymenthistory.exception.RepaymentException;
import com.owl.payrit.domain.repaymenthistory.repository.RepaymentHistoryRepository;
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
            throw new RepaymentException(RepaymentErrorCode.REPAYMENT_STATUS_ERROR);
        }

        if(!paper.getCreditor().equals(member)) {
            throw new RepaymentException(RepaymentErrorCode.REPAYMENT_ONLY_ACCESS_CREDITOR);
        }

        if(requestDate.isBefore(repaymentStartDate) || requestDate.isAfter(repaymentEndDate)) {
            throw new RepaymentException(RepaymentErrorCode.REPAYMENT_NOT_VALID_DATE);
        }

        if(paper.getPaperAmount().getRemainingAmount() < repaymentRequest.repaymentAmount()) {
            throw new RepaymentException(RepaymentErrorCode.REPAYMENT_AMOUNT_OVER);
        }
    }

    @Transactional
    public void remove(Member member, PromissoryPaper paper, RepaymentHistory repaymentHistory) {

        if(!member.equals(paper.getCreditor())) {
            throw new RepaymentException(RepaymentErrorCode.REPAYMENT_ONLY_ACCESS_CREDITOR);
        }

        repaymentHistoryRepository.delete(repaymentHistory);
    }

    public RepaymentHistory getById(Long historyId) {

        return repaymentHistoryRepository.findById(historyId).orElseThrow(
                () -> new RepaymentException(RepaymentErrorCode.REPAYMENT_HISTORY_NOT_FOUND));
    }
}
