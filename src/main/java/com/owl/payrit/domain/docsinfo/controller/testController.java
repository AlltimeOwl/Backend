package com.owl.payrit.domain.docsinfo.controller;

import com.owl.payrit.domain.auth.dto.response.LoginUser;
import com.owl.payrit.domain.docsinfo.service.DocsInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class testController {

    private final DocsInfoService docsInfoService;

    @PostMapping("/test/file")
    public ResponseEntity<String> test(@AuthenticationPrincipal LoginUser loginUser,
                                       @RequestPart("file") MultipartFile file) throws IOException {

        String s = docsInfoService.uploadFile(file);

        return ResponseEntity.ok(s);
    }
}
