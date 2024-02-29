package com.owl.payrit.domain.promissorynote.repository;

import com.owl.payrit.domain.promissorynote.entity.PromissoryNote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromissoryNoteRepository extends JpaRepository<PromissoryNote, Long> {
}
