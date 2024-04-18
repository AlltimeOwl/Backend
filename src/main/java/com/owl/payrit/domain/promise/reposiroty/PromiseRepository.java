package com.owl.payrit.domain.promise.reposiroty;

import com.owl.payrit.domain.promise.entity.Promise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromiseRepository extends JpaRepository<Promise, Long> {
}
