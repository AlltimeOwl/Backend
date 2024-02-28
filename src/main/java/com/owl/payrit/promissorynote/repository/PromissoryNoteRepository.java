package com.owl.payrit.promissorynote.repository;

import com.owl.payrit.promissorynote.entity.PromissoryNote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromissoryNoteRepository extends JpaRepository<PromissoryNote, Long> {
}
