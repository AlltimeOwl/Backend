package com.owl.payrit.promissorynote.service;

import com.owl.payrit.promissorynote.repository.PromissoryNoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PromissoryNoteService {

    private final PromissoryNoteRepository promissoryNoteRepository;

}
