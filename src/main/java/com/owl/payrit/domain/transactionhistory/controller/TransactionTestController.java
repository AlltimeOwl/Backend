package com.owl.payrit.domain.transactionhistory.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TransactionTestController {

    //FIXME: 결제 테스트를 위한 컨트롤러. 삭제 필요

    @GetMapping("/payment")
    public String getTestPay(Model model) {



        return "testPay";
    }
}
