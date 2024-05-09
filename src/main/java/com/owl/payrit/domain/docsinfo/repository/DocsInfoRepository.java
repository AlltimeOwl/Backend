package com.owl.payrit.domain.docsinfo.repository;

import com.owl.payrit.domain.docsinfo.entity.DocsInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DocsInfoRepository extends JpaRepository<DocsInfo, Long> {

    boolean existsByDocsKey(String docsKey);
}
