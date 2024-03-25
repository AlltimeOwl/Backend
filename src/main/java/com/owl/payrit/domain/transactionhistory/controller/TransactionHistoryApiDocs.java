package com.owl.payrit.domain.transactionhistory.controller;

import com.owl.payrit.domain.auth.dto.response.LoginUser;
import com.owl.payrit.domain.transactionhistory.dto.request.TransactionHistorySaveRequest;
import com.owl.payrit.domain.transactionhistory.dto.response.TransactionHistoryDetailResponse;
import com.owl.payrit.domain.transactionhistory.dto.response.TransactionHistoryListResponse;
import com.owl.payrit.domain.transactionhistory.exception.TransactionHistoryErrorCode;
import com.owl.payrit.global.swagger.annotation.ApiErrorCodeExample;
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

import java.util.List;

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

    @Operation(summary = "결제 내역 상세 조회 API", description = "결제 내역의 상세 조회를 시도합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회에 성공하여 결제 상세 정보를 받아옵니다.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TransactionHistoryDetailResponse.class))
                    }),
            @ApiResponse(responseCode = "403", description = "결제 내역에 접근할 권한이 없습니다.")
    })
    ResponseEntity<TransactionHistoryDetailResponse> detail(@AuthenticationPrincipal LoginUser loginUser,
                                                            @Parameter(description = "결제 내역 id", required = true) Long id);

    @Operation(summary = "결제 내역 목록 조회 API", description = "내가 가진 결제 내역을 리스트 형태로 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회에 성공하여 나의 결제 내역 리스트를 받아옵니다.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TransactionHistoryListResponse.class))
                    })
    })
    ResponseEntity<List<TransactionHistoryListResponse>> list(@AuthenticationPrincipal LoginUser loginUser);
}
