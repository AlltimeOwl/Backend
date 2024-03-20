package com.owl.payrit.domain.transactionhistory.repository;

import com.owl.payrit.domain.member.entity.Member;
import com.owl.payrit.domain.transactionhistory.entity.TransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long> {

    List<TransactionHistory> findAllByPaidMember(Member paidMember);
}
