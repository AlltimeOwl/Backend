package com.owl.payrit.domain.docsinfo.service;

import com.owl.payrit.domain.docsinfo.config.AzureStorageConfigProps;
import com.owl.payrit.domain.docsinfo.entity.DocsInfo;
import com.owl.payrit.domain.docsinfo.exception.DocsInfoErrorCode;
import com.owl.payrit.domain.docsinfo.exception.DocsInfoException;
import com.owl.payrit.domain.docsinfo.repository.DocsInfoRepository;
import com.owl.payrit.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.azure.storage.file.share.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DocsInfoService {

    private final AzureStorageConfigProps storageConfigProps;

    private final DocsInfoRepository docsInfoRepository;

    @Transactional
    public Long createByWriter(Member writer, String writerIpAddr, String writerCI,
                               MultipartFile documentFile) throws IOException {

        DocsInfo docsInfo = DocsInfo.builder()
                .writer(writer)
                .writerIpAddr(writerIpAddr)
                .writerCI("작성자의 본인인증 CI 값이 저장됩니다.")         //FIXME: Need to Member CI
                .createdAt(LocalDateTime.now())
                .docsUrl(uploadFile(documentFile))
                .build();

        DocsInfo savedDocsInfo = docsInfoRepository.save(docsInfo);

        return savedDocsInfo.getId();
    }

    @Transactional
    public void acceptByAccepter(DocsInfo docsInfo, Member accepter, String accepterIpAddr, String accepterCI,
                                 MultipartFile documentFile) throws IOException {

        DocsInfo completedDocsInfo = docsInfo.toBuilder()
                .accepter(accepter)
                .accepterIpAddr(accepterIpAddr)
                .accepterCI(accepterCI)
                .acceptedAt(LocalDateTime.now())
                .docsKey(getUniqueDocsKey())
                .docsUrl(uploadFile(documentFile))
                .build();

        docsInfoRepository.save(completedDocsInfo);
    }

    private String getUniqueDocsKey() {
        String docsKey = UUID.randomUUID().toString();
        return docsInfoRepository.existsByDocsKey(docsKey) ? getUniqueDocsKey() : docsKey;
    }

    public String uploadFile(MultipartFile documentFile) throws IOException {

        try {
            String connectStr = storageConfigProps.getConnectStr();
            String shareName = storageConfigProps.getShareName();
            String dirName = storageConfigProps.getDirName();
            String fileName = documentFile.getOriginalFilename();

            ShareDirectoryClient dirClient = new ShareFileClientBuilder()
                    .connectionString(connectStr).shareName(shareName)
                    .resourcePath(dirName)
                    .buildDirectoryClient();

            ShareFileClient fileClient = dirClient.getFileClient(fileName);

            byte[] fileBytes = documentFile.getInputStream().readAllBytes();

            fileClient.create(fileBytes.length);
            fileClient.uploadRange(new ByteArrayInputStream(fileBytes), fileBytes.length);

            return fileClient.getFileUrl();

        } catch (Exception e) {
            log.error("uploadFile exception: " + e.getMessage());
            throw new DocsInfoException(DocsInfoErrorCode.DOCS_BAD_REQUEST);
        }
    }
}
