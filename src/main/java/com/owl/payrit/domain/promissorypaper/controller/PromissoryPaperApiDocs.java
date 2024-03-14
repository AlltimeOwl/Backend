package com.owl.payrit.domain.promissorypaper.controller;

import com.owl.payrit.domain.auth.domain.OauthProvider;
import com.owl.payrit.domain.auth.dto.request.LoginTokenRequest;
import com.owl.payrit.domain.auth.dto.response.LoginUser;
import com.owl.payrit.domain.auth.dto.response.TokenResponse;
import com.owl.payrit.domain.promissorypaper.dto.request.PaperWriteRequest;
import com.owl.payrit.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "차용증 관련 API", description = "차용증 서비스 API 입니다.")
public interface PromissoryPaperApiDocs {

    @Operation(summary = "차용증 작성 API", description = "차용증 관련 데이터를 입력해 request 하면, 차용증 작성이 완료됩니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 처리되었습니다."),
            @ApiResponse(responseCode = "400", description = "차용증 정보가 유효하지 않거나, 회원의 정보와 일치하지 않습니다.\n" +
                    "이자율 20% 제한, 작성 시기, 작성자 역할 등")
    })
    ResponseEntity<String> write(@AuthenticationPrincipal LoginUser loginUser,
                                 @RequestBody @Schema(implementation = PaperWriteRequest.class) PaperWriteRequest paperWriteRequest
    );
}
