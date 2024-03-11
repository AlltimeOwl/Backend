package com.owl.payrit.domain.notification.repository;

import com.owl.payrit.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
