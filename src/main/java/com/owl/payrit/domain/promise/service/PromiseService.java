package com.owl.payrit.domain.promise.service;

import com.owl.payrit.domain.promise.reposiroty.PromiseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PromiseService {

    private final PromiseRepository promiseRepository;
}
