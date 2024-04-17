package com.owl.payrit.domain.notification.service;

import com.owl.payrit.domain.member.entity.Member;
import com.owl.payrit.domain.member.service.MemberService;
import com.owl.payrit.domain.notification.entity.Notification;
import com.owl.payrit.domain.notification.entity.NotificationMessage;
import com.owl.payrit.domain.notification.event.NotificationEvent;
import com.owl.payrit.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationHelper {

    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;
    private final MemberService memberService;

    @Transactional
    public void generateNotification(Long targetId, NotificationMessage notificationMessage, String[] args) {
        Member member = memberService.findById(targetId);
        if(!checkNotificationAvailable(member)) return;
        save(notificationMessage, member, args);
        notificationService.push(notificationMessage, args, member.getFirebaseToken());
    }

    public void generateNotification(NotificationEvent notificationEvent) {
        generateNotification(notificationEvent.targetId(), notificationEvent.notificationMessage(), notificationEvent.messageArgs());
    }

    private boolean checkNotificationAvailable(Member member) {
        return member.isAgreeNotification() && member.getFirebaseToken() != null;
    }

    private void save(NotificationMessage notificationMessage, Member member, String[] args) {
        String message = notificationMessage.generateMessage(args);
        Notification notification = Notification.builder()
                                                .member(member)
                                                .notificationType(notificationMessage.getNotificationType())
                                                .contents(message)
                                                .build();
        notificationRepository.save(notification);
    }

    /*
    TODO
    FCM 의존성
    알람 전송 기능 (제목, 내용 생성)
    알람 리스트 조회 기능
    알람 조회 기능 (read = true)
     */

}
