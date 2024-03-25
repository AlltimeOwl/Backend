package com.owl.payrit.domain.docsinfo.service;

import com.owl.payrit.domain.docsinfo.repository.DocsInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DocsInfoService {

    private final DocsInfoRepository docsInfoRepository;
}
