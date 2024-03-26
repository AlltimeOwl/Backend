package com.owl.payrit.domain.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.owl.payrit.domain.member.entity.Member;
import com.owl.payrit.domain.notification.entity.NotificationMessage;
import com.owl.payrit.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationPushService {

    private final NotificationRepository notificationRepository;

    @Async("notificationThreadPoolBean")
    public void push(NotificationMessage notificationMessage, String[] args, Member member) {

        String title = notificationMessage.getNotificationType().getDescription();
        String body = notificationMessage.generateMessage(args);

        Notification notification = Notification.builder()
                                                .setTitle(title)
                                                .setBody(body)
                                                .build();

        Message message = Message.builder()
                                 .setNotification(notification)
                                 .setToken(member.getFirebaseToken())
                                 .build();

        FirebaseMessaging.getInstance().sendAsync(message);
    }
}
