package com.owl.payrit.domain.repaymenthistory.service;

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
}
