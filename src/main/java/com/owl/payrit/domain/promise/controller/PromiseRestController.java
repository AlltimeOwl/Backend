package com.owl.payrit.domain.promise.controller;


import com.owl.payrit.domain.promise.service.PromiseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/paper")
public class PromiseRestController {

    private final PromiseService promiseService;
}
