package com.owl.payrit.domain.promissorypaper.repository;

import com.owl.payrit.domain.member.entity.Member;
import com.owl.payrit.domain.promissorypaper.entity.PaperStatus;
import com.owl.payrit.domain.promissorypaper.entity.PromissoryPaper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PromissoryPaperRepository extends JpaRepository<PromissoryPaper, Long> {

    List<PromissoryPaper> findAllByCreditor(Member creditor);
    List<PromissoryPaper> findAllByDebtor(Member debtor);
    List<PromissoryPaper> findAllByUpdatedAtBeforeAndPaperStatus(LocalDateTime expireStandardDate, PaperStatus paperStatus);
    @Query("SELECT p FROM PromissoryPaper p WHERE p.creditor = :member OR p.debtor = :member OR p.writer = :member")
    List<PromissoryPaper> findAllByCreditorOrDebtorOrWriter(@Param("member") Member member);
}
