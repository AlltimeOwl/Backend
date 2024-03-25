package com.owl.payrit.domain.docsinfo.service;

import com.owl.payrit.domain.docsinfo.entity.DocsInfo;
import com.owl.payrit.domain.docsinfo.repository.DocsInfoRepository;
import com.owl.payrit.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DocsInfoService {

    private final DocsInfoRepository docsInfoRepository;

    @Transactional
    public Long createByWriter(Member writer, String writerIpAddr, String writerCI) {

        DocsInfo docsInfo = DocsInfo.builder()
                .writer(writer)
                .writerIpAddr(writerIpAddr)
                .writerCI("작성자의 본인인증 CI 값이 저장됩니다.")         //FIXME: Need to Member CI
                .createdAt(LocalDateTime.now())
                .build();

        DocsInfo savedDocsInfo = docsInfoRepository.save(docsInfo);

        return savedDocsInfo.getId();
    }

    @Transactional
    public void acceptByAccepter(DocsInfo docsInfo, Member accepter, String accepterIpAddr, String accepterCI) {

        DocsInfo completedDocsInfo = docsInfo.toBuilder()
                .accepter(accepter)
                .accepterIpAddr(accepterIpAddr)
                .accepterCI(accepterCI)
                .acceptedAt(LocalDateTime.now())
                .docsKey(getUniqueDocsKey())
                .docsUrl(uploadToStorage())
                .build();

        docsInfoRepository.save(completedDocsInfo);
    }

    private String getUniqueDocsKey() {
        String docsKey = UUID.randomUUID().toString();
        return docsInfoRepository.existsByDocsKey(docsKey) ? getUniqueDocsKey() : docsKey;
    }

    private String uploadToStorage() {


        //TODO


        return "Storage URL";
    }
}
