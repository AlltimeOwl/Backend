package com.owl.payrit.domain.promissorynote.controller;

import com.owl.payrit.domain.promissorynote.service.PromissoryNoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PromissoryNoteRestController {

    private final PromissoryNoteService promissoryNoteService;

}
