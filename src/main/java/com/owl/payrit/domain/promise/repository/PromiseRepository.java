package com.owl.payrit.domain.promise.repository;

import com.owl.payrit.domain.member.entity.Member;
import com.owl.payrit.domain.promise.entity.Promise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PromiseRepository extends JpaRepository<Promise, Long> {

    List<Promise> findAllByWriter(Member writer);
    List<Promise> findAllByOwner(Member owner);
}
