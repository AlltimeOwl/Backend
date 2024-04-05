package com.owl.payrit.domain.transactionhistory.controller;

import com.owl.payrit.domain.member.service.MemberService;
import com.owl.payrit.domain.transactionhistory.dto.response.PaymentInfoResponse;
import com.owl.payrit.domain.transactionhistory.entity.TransactionType;
import com.owl.payrit.domain.transactionhistory.service.TransactionHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class TransactionTestController {

    //FIXME: 결제 테스트를 위한 컨트롤러. 삭제 필요

    private final TransactionHistoryService transactionHistoryService;
    private final MemberService memberService;

    @GetMapping("/payment")
    public String getTestPay(Model model) {

        PaymentInfoResponse paymentInfo = transactionHistoryService.getPaymentInfo(1L, TransactionType.PAPER_TRANSACTION);

        model.addAttribute("PID", paymentInfo.PID());
        model.addAttribute("PG", paymentInfo.PGCode());
        model.addAttribute("merchantUID", paymentInfo.merchantUID());
        model.addAttribute("name", paymentInfo.name());
        model.addAttribute("amount", paymentInfo.amount());
        model.addAttribute("buyerName", paymentInfo.buyerName());
        model.addAttribute("buyerEmail", paymentInfo.buyerEmail());
        model.addAttribute("buyerTel", paymentInfo.buyerTel());

        return "testPay";
    }
}
