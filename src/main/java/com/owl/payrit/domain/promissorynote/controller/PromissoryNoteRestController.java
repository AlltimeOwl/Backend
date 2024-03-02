package com.owl.payrit.domain.promissorynote.controller;

import com.owl.payrit.domain.promissorynote.dto.request.PromissoryNoteRequest;
import com.owl.payrit.domain.promissorynote.service.PromissoryNoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/paper")
public class PromissoryNoteRestController {

    private final PromissoryNoteService promissoryNoteService;

    @PostMapping("/write")
    public ResponseEntity<String> createNote(@RequestBody PromissoryNoteRequest promissoryNoteRequest,
                                             Principal principal) {

        promissoryNoteService.createNote(principal, promissoryNoteRequest);

        return ResponseEntity.ok().build();
    }

}
