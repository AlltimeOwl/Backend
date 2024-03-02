package com.owl.payrit.domain.promissorynote.service;

import com.owl.payrit.domain.promissorynote.dto.request.PaperWriteRequest;
import com.owl.payrit.domain.promissorynote.dto.request.PaperWriteRequest2;
import com.owl.payrit.domain.promissorynote.entity.PromissoryNote;
import com.owl.payrit.domain.promissorynote.repository.PromissoryNoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class PromissoryNoteService {

    private final PromissoryNoteRepository promissoryNoteRepository;

    @Transactional
    public void createNote(PaperWriteRequest paperWriteRequest) {

        //FIXME: 로그인한 사용자 가져오기 (Principal -> Member)

        /*TODO
        빌려줄 예정이에요 OR 빌릴 예정이에요
        -> 내 정보 입력, 상대방 정보 입력
        작성자가 어떤 역할이냐에 따라 로그인한 사용자가 creditor OR debtor */

        PromissoryNote promissoryNote = PromissoryNote.builder()
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

        promissoryNoteRepository.save(promissoryNote);
    }

    @Transactional
    public void createNote2(PaperWriteRequest2 paperWriteRequest2) {

        List<Map<String, String>> infoMapList = getInfoMapList(paperWriteRequest2);

        Map<String, String> creditorMap = infoMapList.get(0);
        Map<String, String> debtorMap = infoMapList.get(1);

        log.info(creditorMap.toString());
        log.info(debtorMap.toString());

        PromissoryNote promissoryNote = PromissoryNote.builder()
                .amount(paperWriteRequest2.amount())
                .transactionDate(paperWriteRequest2.transactionDate())
                .repaymentStartDate(paperWriteRequest2.repaymentStartDate())
                .repaymentEndDate(paperWriteRequest2.repaymentEndDate())
                .specialConditions(paperWriteRequest2.specialConditions())
                .creditorPhoneNumber(creditorMap.get("phoneNumber"))
                .creditorAddress(creditorMap.get("address"))
                .debtorPhoneNumber(debtorMap.get("phoneNumber"))
                .debtorAddress(debtorMap.get("address"))
                .build();

        promissoryNoteRepository.save(promissoryNote);
    }

    public List<Map<String, String>> getInfoMapList(PaperWriteRequest2 paperWriteRequest2) {

        List<Map<String, String>> infoMapList = new ArrayList<>();

        Map<String, String> creditorMap = new HashMap<>();
        Map<String, String> debtorMap = new HashMap<>();

        if(paperWriteRequest2.isCreditor()){
            creditorMap = setInfoMap(paperWriteRequest2.myName(),
                    paperWriteRequest2.myPhoneNumber(), paperWriteRequest2.myAddress());
            debtorMap = setInfoMap(paperWriteRequest2.yourName(),
                    paperWriteRequest2.yourPhoneNumber(), paperWriteRequest2.yourAddress());
        } else {
            creditorMap = setInfoMap(paperWriteRequest2.yourName(),
                    paperWriteRequest2.yourPhoneNumber(), paperWriteRequest2.yourAddress());
            debtorMap = setInfoMap(paperWriteRequest2.myName(),
                    paperWriteRequest2.myPhoneNumber(), paperWriteRequest2.myAddress());
        }

        infoMapList.add(creditorMap);
        infoMapList.add(debtorMap);

        return infoMapList;
    }

    public Map<String, String> setInfoMap(String name, String phoneNumber, String address) {

        Map<String, String> infoMap = new HashMap<>();

        infoMap.put("name", name);
        infoMap.put("phoneNumber", phoneNumber);
        infoMap.put("address", address);

        return infoMap;
    }
}