package com.owl.payrit.domain.notification.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationMessage {
    WELCOME(NotificationType.GENERAL, 1, "%s님, 페이릿에 오신 것을 환영해요!"),
    ID_VERIFICATION(NotificationType.GENERAL, 1, "%s님, 본인인증이 필요해요."),
    APPROVAL_REQUEST(NotificationType.APPROVAL_REQUEST, 2, "%s님, %s님으로부터 페이릿 승인 요청이 왔어요."),
    AFTER_APPROVAL_REQUEST(NotificationType.GENERAL, 1, "%s님에게 차용증 승인요청을 했어요. 승인 재요청을 원하시면 <홈-> 페이릿 카드>를 눌러 재요청해 보세요."),
    MODIFY_REQUEST(NotificationType.MODIFY_REQUEST, 1, "%s님이 페이릿 수정을 요청했어요. 해당 알림을 누르면 수정하러 갈 수 있어요."),
    MODIFICATION_COMPLETE(NotificationType.MODIFY_REQUEST, 1, "%s님이 페이릿 수정을 완료했어요. 해당 알림을 누르면 수락하러 갈 수 있어요."),
    PAYMENT_REQUIRED(NotificationType.PAYMENT_REQUEST, 1, "%s님, 아직 거래 중인 페이릿이 있어요. 해당 알림을 누르면 결제하러 갈 수 있어요."),
    PAYMENT_TIMEOUT(NotificationType.PAYMENT_REQUEST, 2, "%s님, 아직 %s님이 수락하지 않았어요. 해당 알림을 누르면 재요청하러 갈 수 있어요."),
    PAYMENT_COMPLETE(NotificationType.PAYMENT_REQUEST, 2, "%s님과 %s님의 페이릿이 완성됐어요! 거래된 페이릿은 홈에서 확인 가능해요."),
    REPAYMENT_DUE(NotificationType.REPAYMENT_REQUEST, 3, "%s님 %s 뒤면 돈을 돌려줘야 하는 날이에요! %s님과의 약속을 기억해 주세요."),
    FULL_REPAYMENT_RECEIVED(NotificationType.REPAYMENT_REQUEST, 3, "%s님! %s님의 전체 상환으로 페이릿 거래가 끝났어요! %s님께 소중한 돈을 빌려줘서 고마워요."),
    FULL_REPAYMENT_MADE(NotificationType.REPAYMENT_REQUEST, 2, "%s님! 전체 상환으로 %s님과의 페이릿이 만료됐어요! 약속을 지켜줘서 고마워요."),
    PARTIAL_REPAYMENT(NotificationType.REPAYMENT_REQUEST, 2, "%s님과의 페이릿에서 일부 상환 %s 원 기록이 기록됐어요."),
    PARTIAL_REPAYMENT_CANCELLED(NotificationType.REPAYMENT_REQUEST, 2, "%s님과의 페이릿에서 일부 상환 %s 원 기록이 삭제됐어요."),
    UNPAID(NotificationType.REPAYMENT_REQUEST, 0, "자니? 우리의 약속 기한이 얼마 남지 않았단 거 알고 있지?");

    private final NotificationType notificationType;
    private final int numArgs;
    private final String message;

    public String generateMessage(String... args) {
        if(args.length != numArgs) {
            throw new IllegalArgumentException("알람 인자가 일치하지 않습니다.");
        }

        return String.format(message, (Object[]) args);
    }
}
