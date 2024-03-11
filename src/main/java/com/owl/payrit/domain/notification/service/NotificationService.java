package com.owl.payrit.domain.notification.service;

import com.owl.payrit.domain.member.entity.Member;
import com.owl.payrit.domain.notification.entity.Notification;
import com.owl.payrit.domain.notification.entity.NotificationType;
import com.owl.payrit.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional
    public void create(Member member, String contents, NotificationType notificationType) {

        Notification notification = new Notification(member, contents, notificationType);

        notificationRepository.save(notification);
    }

}
