package com.owl.payrit.domain.promissorynote.service;

import com.owl.payrit.domain.promissorynote.dto.request.PromissoryNoteRequest;
import com.owl.payrit.domain.promissorynote.entity.PromissoryNote;
import com.owl.payrit.domain.promissorynote.repository.PromissoryNoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class PromissoryNoteService {

    private final PromissoryNoteRepository promissoryNoteRepository;

    @Transactional
    public void createNote(Principal principal, PromissoryNoteRequest promissoryNoteRequest) {

        //FIXME: 로그인한 사용자 가져오기 (Principal -> Member)

        /*TODO
        빌려줄 예정이에요 OR 빌릴 예정이에요
        -> 내 정보 입력, 상대방 정보 입력
        작성자가 어떤 역할이냐에 따라 로그인한 사용자가 creditor OR debtor */

        PromissoryNote promissoryNote = PromissoryNote.builder()
                .amount(promissoryNoteRequest.amount())
                .transactionDate(promissoryNoteRequest.transactionDate())
                .repaymentStartDate(promissoryNoteRequest.repaymentStartDate())
                .repaymentEndDate(promissoryNoteRequest.repaymentEndDate())
                .specialConditions(promissoryNoteRequest.specialConditions())
                .creditorPhoneNumber(promissoryNoteRequest.creditorPhoneNumber())
                .creditorAddress(promissoryNoteRequest.creditorAddress())
                .debtorPhoneNumber(promissoryNoteRequest.debtorPhoneNumber())
                .debtorAddress(promissoryNoteRequest.debtorAddress())
                .build();

        promissoryNoteRepository.save(promissoryNote);
    }
}
