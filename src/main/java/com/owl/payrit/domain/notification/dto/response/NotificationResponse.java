package com.owl.payrit.domain.notification.dto.response;

import com.owl.payrit.domain.notification.entity.Notification;
import java.time.LocalDateTime;

public record NotificationResponse(
    String title,
    String content,
    boolean isRead,
    LocalDateTime createdAt
) {
    public static NotificationResponse of(Notification notification) {
        return new NotificationResponse(notification.getNotificationType().getDescription(), notification.getContents(), notification.isRead() ,notification.getCreatedAt());
    }
}
