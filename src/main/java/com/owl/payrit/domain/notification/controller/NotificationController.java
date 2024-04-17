package com.owl.payrit.domain.notification.controller;

import com.owl.payrit.domain.auth.dto.response.LoginUser;
import com.owl.payrit.domain.notification.dto.response.NotificationResponse;
import com.owl.payrit.domain.notification.service.NotificationHelper;
import com.owl.payrit.domain.notification.service.NotificationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notification")
public class NotificationController implements NotificationApiDocs{

    private final NotificationHelper notificationHelper;
    private final NotificationService notificationService;

    @GetMapping("/")
    public ResponseEntity<List<NotificationResponse>> getNotificationList(@AuthenticationPrincipal LoginUser loginUser) {
        log.info("{} member request notificationList", loginUser.oauthInformation().getOauthProviderId());
        List<NotificationResponse> notificationList = notificationService.findNotificationList(loginUser);
        return ResponseEntity.ok().body(notificationList);
    }
}
