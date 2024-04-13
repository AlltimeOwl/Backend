package com.owl.payrit.domain.transactionhistory.entity;

import com.owl.payrit.domain.member.entity.Member;
import com.owl.payrit.domain.promissorypaper.entity.PromissoryPaper;
import com.owl.payrit.global.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
public class TransactionHistory extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Member paidMember;

    @ManyToOne(fetch = FetchType.LAZY)
    private PromissoryPaper linkedPaper;

    private LocalDateTime transactionDate;

    private long amount;

    private String contents;            //결제 내용

    private String paymentMethod;       //결제 수단

    private String applyNum;      //승인 번호

    private String impUid;              //주문 번호

    private String merchantUid;         //고유 내부 상품 번호

    private boolean isSuccess;          //결제 성공 여부
}