package com.owl.payrit.domain.memo.repository;

import com.owl.payrit.domain.memo.entity.Memo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoRepository extends JpaRepository<Memo, Long> {

    List<Memo> findAllByMemberIdAndPromissoryPaperIdOrderByCreatedAt(long memberId, long promissoryPaperId);
}
