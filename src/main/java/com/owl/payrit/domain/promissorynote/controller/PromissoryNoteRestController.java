package com.owl.payrit.domain.promissorynote.controller;

import com.owl.payrit.domain.promissorynote.dto.request.PaperWriteRequest;
import com.owl.payrit.domain.promissorynote.service.PromissoryNoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/paper")
public class PromissoryNoteRestController {

    private final PromissoryNoteService promissoryNoteService;

    @PostMapping("/write")
    public ResponseEntity<String> createNote(@RequestBody PaperWriteRequest paperWriteRequest) {

        log.info(paperWriteRequest.toString());

        promissoryNoteService.createNote(paperWriteRequest);

        return ResponseEntity.ok().body("write");
    }

}
