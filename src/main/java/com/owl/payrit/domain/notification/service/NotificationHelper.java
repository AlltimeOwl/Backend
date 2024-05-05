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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationHelper {

    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;
    private final MemberService memberService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void generateNotification(Long targetId, NotificationMessage notificationMessage, String[] args) {
        Member member = memberService.findById(targetId);
        if(!checkNotificationAvailable(member)) {
            log.info("회원이 알람을 동의하지 않았거나, 파이어베이스 토큰이 없어 알람 전송에 실패했습니다.");
            return;
        }
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
}
