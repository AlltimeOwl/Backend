package com.owl.payrit.domain.docsinfo.repository;

import com.owl.payrit.domain.docsinfo.entity.DocsInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocsInfoRepository extends JpaRepository<DocsInfo, Long> {
}
