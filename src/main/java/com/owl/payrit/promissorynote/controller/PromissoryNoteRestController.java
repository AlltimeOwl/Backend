package com.owl.payrit.promissorynote.controller;

import com.owl.payrit.promissorynote.service.PromissoryNoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PromissoryNoteRestController {

    private final PromissoryNoteService promissoryNoteService;

}
