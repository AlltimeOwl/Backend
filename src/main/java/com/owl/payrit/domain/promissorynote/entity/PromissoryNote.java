package com.owl.payrit.domain.promissorynote.entity;

import com.owl.payrit.global.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
public class PromissoryNote extends BaseEntity {

//    private Member creditor;
    private String creditorPhoneNumber;
    private boolean isCreditorAgree;
    private String creditorAddress;

//    private Member debtor;
    private String debtorPhoneNumber;
    private boolean isDebtorAgree;
    private String debtorAddress;
    
    private long amount;
    private int interestRate;
    
    private LocalDate transactionDate;
    private LocalDate repaymentStartDate;
    private LocalDate repaymentEndDate;
    private String specialConditions;
    
    //현재 상환액
    //저장소 URL
    
    private boolean isPaid;

    private String noteKey;    //차용증 고유 값

    //누가 작성을 요청했는지
    
    //차용증 상태
}
