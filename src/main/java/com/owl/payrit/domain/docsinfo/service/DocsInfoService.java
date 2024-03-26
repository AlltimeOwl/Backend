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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DocsInfoService {

    private final AzureStorageConfigProps storageConfigProps;

    private final DocsInfoRepository docsInfoRepository;

    @Transactional
    public DocsInfo createByWriter(Member writer, String writerIpAddr, String writerCI) throws IOException {

        DocsInfo docsInfo = DocsInfo.builder()
                .writer(writer)
                .writerIpAddr(writerIpAddr)
                .writerCI(writerCI)         //FIXME: Need to Member CI
                .createdAt(LocalDateTime.now())
                .build();

        return docsInfoRepository.save(docsInfo);
    }

    @Transactional
    public void acceptByAccepter(DocsInfo docsInfo, Member accepter, String accepterIpAddr, String accepterCI,
                                 MultipartFile documentFile) throws IOException {

        String docsName = genDocsNameByPaper(docsInfo.getWriter(), accepter);

        DocsInfo completedDocsInfo = docsInfo.toBuilder()
                .accepter(accepter)
                .accepterIpAddr(accepterIpAddr)
                .accepterCI(accepterCI)
                .acceptedAt(LocalDateTime.now())
                .docsKey(getUniqueDocsKey())
                .docsUrl(uploadFile(documentFile, docsName))
                .build();

        docsInfoRepository.save(completedDocsInfo);
    }

    private String getUniqueDocsKey() {
        String docsKey = UUID.randomUUID().toString();
        return docsInfoRepository.existsByDocsKey(docsKey) ? getUniqueDocsKey() : docsKey;
    }

    public String uploadFile(MultipartFile documentFile, String docsName) throws IOException {

        try {
            String connectStr = storageConfigProps.getConnectStr();
            String shareName = storageConfigProps.getShareName();
            String dirName = storageConfigProps.getDirName();

            ShareDirectoryClient dirClient = new ShareFileClientBuilder()
                    .connectionString(connectStr).shareName(shareName)
                    .resourcePath(dirName)
                    .buildDirectoryClient();

            ShareFileClient fileClient = dirClient.getFileClient(docsName);

            byte[] fileBytes = documentFile.getInputStream().readAllBytes();

            fileClient.create(fileBytes.length);
            fileClient.uploadRange(new ByteArrayInputStream(fileBytes), fileBytes.length);

            return fileClient.getFileUrl();

        } catch (Exception e) {
            log.error("uploadFile exception: " + e.getMessage());
            throw new DocsInfoException(DocsInfoErrorCode.DOCS_BAD_REQUEST);
        }
    }

    public String genDocsNameByPaper(Member writer, Member accepter) {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("YYYY-MM-dd");

        String formattedDate = LocalDate.now().format(dateTimeFormatter);

        String writerName = writer.getName();
        Long writerId = writer.getId();
        String accepterName = accepter.getName();
        Long accepterId = accepter.getId();

        return writerName + writerId + "_" + formattedDate + "_" + accepterName + accepterId;
    }
}
