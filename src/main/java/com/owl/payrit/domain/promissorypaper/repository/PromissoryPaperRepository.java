package com.owl.payrit.domain.promissorypaper.repository;

import com.owl.payrit.domain.member.entity.Member;
import com.owl.payrit.domain.promissorypaper.entity.PaperStatus;
import com.owl.payrit.domain.promissorypaper.entity.PromissoryPaper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PromissoryPaperRepository extends JpaRepository<PromissoryPaper, Long> {

    Optional<PromissoryPaper> findByPaperKey(String paperKey);
    boolean existsByPaperKey(String paperKey);
    List<PromissoryPaper> findAllByCreditor(Member creditor);
    List<PromissoryPaper> findAllByDebtor(Member debtor);
    List<PromissoryPaper> findAllByRepaymentEndDateAndPaperStatus(LocalDate repaymentEndDate, PaperStatus paperStatus);
    List<PromissoryPaper> findAllByUpdatedAtBeforeAndPaperStatus(LocalDateTime expireStandardDate, PaperStatus paperStatus);
    
    //TODO: QueryDSL를 사용하여 `findAllByCreditorOrDebtor`과 같은 복잡한 옵션 고려
}
