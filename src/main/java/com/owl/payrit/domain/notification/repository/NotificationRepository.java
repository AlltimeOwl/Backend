package com.owl.payrit.domain.notification.repository;

import com.owl.payrit.domain.member.entity.Member;
import com.owl.payrit.domain.notification.entity.Notification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByMemberOrderByCreatedAtDesc(Member member);
}
