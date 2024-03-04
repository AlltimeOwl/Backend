package com.owl.payrit.domain.promissorypaper.service;

import com.owl.payrit.domain.promissorypaper.dto.request.PaperWriteRequest;
import com.owl.payrit.domain.promissorypaper.entity.PromissoryPaper;
import com.owl.payrit.domain.promissorypaper.repository.PromissoryPaperRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PromissoryPaperService {

    private final PromissoryPaperRepository promissoryPaperRepository;

    @Transactional
    public void createNote(PaperWriteRequest paperWriteRequest) {

        PromissoryPaper promissoryPaper = PromissoryPaper.builder()
                .amount(paperWriteRequest.amount())
                .transactionDate(paperWriteRequest.transactionDate())
                .repaymentStartDate(paperWriteRequest.repaymentStartDate())
                .repaymentEndDate(paperWriteRequest.repaymentEndDate())
                .specialConditions(paperWriteRequest.specialConditions())
                .creditorPhoneNumber(paperWriteRequest.creditorPhoneNumber())
                .creditorAddress(paperWriteRequest.creditorAddress())
                .debtorPhoneNumber(paperWriteRequest.debtorPhoneNumber())
                .debtorAddress(paperWriteRequest.debtorAddress())
                .build();

        promissoryPaperRepository.save(promissoryPaper);
    }
}