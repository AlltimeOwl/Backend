package com.owl.payrit.domain.docsinfo.service;

import com.azure.storage.file.share.ShareDirectoryClient;
import com.azure.storage.file.share.ShareFileClient;
import com.azure.storage.file.share.ShareFileClientBuilder;
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
    public DocsInfo createByWriter(Member writer, String writerIpAddr, String writerCI) {

        DocsInfo docsInfo = DocsInfo.builder()
                .writerId(writer.getId())
                .writerName(writer.getName())
                .writerPhoneNum(writer.getPhoneNumber())
                .writerIpAddr(writerIpAddr)
                .writerCI(writerCI)         //FIXME: Need to Member CI
                .createdAt(LocalDateTime.now())
                .build();

        return docsInfoRepository.save(docsInfo);
    }

    @Transactional
    public void acceptByAccepter(DocsInfo docsInfo, Member accepter, String accepterIpAddr, String accepterCI,
                                 MultipartFile documentFile) {

        StringBuilder sb = new StringBuilder();
        sb.append(getUniqueDocsKey());
        sb.append(".pdf");

        DocsInfo completedDocsInfo = docsInfo.toBuilder()
                .accepterId(accepter.getId())
                .accepterName(accepter.getName())
                .accepterPhoneNum(accepter.getPhoneNumber())
                .accepterIpAddr(accepterIpAddr)
                .accepterCI(accepterCI)
                .acceptedAt(LocalDateTime.now())
                .docsKey(getUniqueDocsKey())
                .docsUrl(uploadFile(documentFile, sb.toString()))
                .build();

        docsInfoRepository.save(completedDocsInfo);
    }

    public String uploadFile(MultipartFile documentFile, String docsName) {

        try {
            String connectStr = storageConfigProps.getConnectStr();
            String shareName = storageConfigProps.getShareName();
            String dirName = storageConfigProps.getDirName();

            ShareDirectoryClient dirClient = new ShareFileClientBuilder()
                    .connectionString(connectStr)
                    .shareName(shareName)
                    .resourcePath(dirName)
                    .buildDirectoryClient();

            ShareFileClient fileClient = dirClient.getFileClient(docsName);

            byte[] fileBytes = documentFile.getInputStream().readAllBytes();

            fileClient.create(fileBytes.length);
            fileClient.uploadRange(new ByteArrayInputStream(fileBytes), fileBytes.length);

            return fileClient.getFileUrl();

        } catch (Exception e) {
            log.error("uploadFile exception : " + e.getMessage());
            throw new DocsInfoException(DocsInfoErrorCode.DOCS_BAD_REQUEST);
        }
    }

    private String getUniqueDocsKey() {
        String docsKey = UUID.randomUUID().toString();
        return docsInfoRepository.existsByDocsKey(docsKey) ? getUniqueDocsKey() : docsKey;
    }
}
