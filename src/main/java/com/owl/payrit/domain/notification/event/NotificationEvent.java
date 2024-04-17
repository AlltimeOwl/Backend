package com.owl.payrit.domain.notification.event;

import com.owl.payrit.domain.notification.entity.NotificationMessage;

public record NotificationEvent(
    Long targetId,
    NotificationMessage notificationMessage,
    String[] messageArgs
) {

}
