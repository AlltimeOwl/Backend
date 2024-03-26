package com.owl.payrit.domain.notification.service;

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

    /*
    TODO
    FCM 의존성
    알람 전송 기능 (제목, 내용 생성)
    알람 리스트 조회 기능
    알람 조회 기능 (read = true)
     */

}
