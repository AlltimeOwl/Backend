package com.owl.payrit.domain.notification.event;

import com.owl.payrit.domain.notification.service.NotificationHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
public class NotificationEventHandler {

    private final NotificationHelper notificationHelper;

    @TransactionalEventListener
    public void handleNotificationEvent(NotificationEvent notificationEvent) {
        notificationHelper.generateNotification(notificationEvent);
    }
}
