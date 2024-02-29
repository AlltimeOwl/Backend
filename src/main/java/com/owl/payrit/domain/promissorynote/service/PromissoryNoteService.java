package com.owl.payrit.domain.promissorynote.service;

import com.owl.payrit.domain.promissorynote.repository.PromissoryNoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PromissoryNoteService {

    private final PromissoryNoteRepository promissoryNoteRepository;

}
