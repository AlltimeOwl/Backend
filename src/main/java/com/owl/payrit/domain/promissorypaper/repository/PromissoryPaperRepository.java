package com.owl.payrit.domain.promissorypaper.repository;

import com.owl.payrit.domain.member.entity.Member;
import com.owl.payrit.domain.promissorypaper.entity.PaperStatus;
import com.owl.payrit.domain.promissorypaper.entity.PromissoryPaper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PromissoryPaperRepository extends JpaRepository<PromissoryPaper, Long> {

    Optional<PromissoryPaper> findByPaperKey(String paperKey);
    boolean existsByPaperKey(String paperKey);
    List<PromissoryPaper> findAllByCreditor(Member creditor);
    List<PromissoryPaper> findAllByDebtor(Member debtor);
    List<PromissoryPaper> findAllByRepaymentEndDateAndPaperStatus(LocalDate repaymentEndDate, PaperStatus paperStatus);
    List<PromissoryPaper> findAllByUpdatedAtBeforeAndPaperStatus(LocalDateTime expireStandardDate, PaperStatus paperStatus);
    @Query("SELECT p FROM PromissoryPaper p WHERE p.creditor = :member OR p.debtor = :member OR p.writer = :member")
    List<PromissoryPaper> findAllByCreditorOrDebtorOrWriter(@Param("member") Member member);
    
    //TODO: QueryDSL를 사용하여 `findAllByCreditorOrDebtor`과 같은 복잡한 옵션 고려
}
