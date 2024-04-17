package com.owl.payrit.domain.notification.controller;

import com.owl.payrit.domain.auth.dto.response.LoginUser;
import com.owl.payrit.domain.notification.dto.response.NotificationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Tag(name = "알림 조회 API", description = "알림 조회 API 입니다.")
public interface NotificationApiDocs {

    @Operation(summary = "알림 리스트 조회 API", description = "알림 내역 리스트를 조회합니다.")
    @ApiResponse(responseCode = "200",
        content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = NotificationResponse.class))
        })
    ResponseEntity<List<NotificationResponse>> getNotificationList(@AuthenticationPrincipal LoginUser loginUser);
}
