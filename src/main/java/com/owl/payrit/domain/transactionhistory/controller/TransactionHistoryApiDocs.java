package com.owl.payrit.domain.transactionhistory.controller;

import com.owl.payrit.domain.auth.dto.response.LoginUser;
import com.owl.payrit.domain.promissorypaper.dto.request.PaperWriteRequest;
import com.owl.payrit.domain.promissorypaper.exception.PromissoryPaperErrorCode;
import com.owl.payrit.domain.transactionhistory.dto.request.TransactionHistorySaveRequest;
import com.owl.payrit.domain.transactionhistory.exception.TransactionHistoryErrorCode;
import com.owl.payrit.global.swagger.annotation.ApiErrorCodeExample;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "결제 내역 관련 API", description = "결제 내역 API 입니다.")
public interface TransactionHistoryApiDocs {

    @Operation(summary = "결제 내역 저장 API", description = "차용증 결제 내역을 저장합니다.")
    @ApiErrorCodeExample(TransactionHistoryErrorCode.class)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "성공적으로 결제 내역이 저장되었습니다."),
    })
    ResponseEntity<Void> save(@AuthenticationPrincipal LoginUser loginUser,
                              @RequestBody @Schema(implementation = TransactionHistorySaveRequest.class)
                              TransactionHistorySaveRequest transactionHistorySaveRequest
    );


}
