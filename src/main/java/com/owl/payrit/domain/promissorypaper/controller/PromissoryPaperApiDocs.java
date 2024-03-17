package com.owl.payrit.domain.promissorypaper.controller;

import com.owl.payrit.domain.auth.dto.response.LoginUser;
import com.owl.payrit.domain.promissorypaper.dto.request.PaperModifyRequest;
import com.owl.payrit.domain.promissorypaper.dto.request.PaperWriteRequest;
import com.owl.payrit.domain.promissorypaper.dto.response.PaperDetailResponse;
import com.owl.payrit.domain.promissorypaper.dto.response.PaperListResponse;
import com.owl.payrit.domain.promissorypaper.exception.PromissoryPaperErrorCode;
import com.owl.payrit.domain.repaymenthistory.dto.request.RepaymentCancelRequest;
import com.owl.payrit.domain.repaymenthistory.dto.request.RepaymentRequest;
import com.owl.payrit.domain.repaymenthistory.exception.RepaymentErrorCode;
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

@Tag(name = "차용증 관련 API", description = "차용증 서비스 API 입니다.")
public interface PromissoryPaperApiDocs {

    @Operation(summary = "차용증 작성 API", description = "차용증 관련 데이터를 입력해 request 하면, 차용증 작성이 완료됩니다.")
    @ApiErrorCodeExample(PromissoryPaperErrorCode.class)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "성공적으로 작성되었습니다."),
    })
    ResponseEntity<Void> write(@AuthenticationPrincipal LoginUser loginUser,
                               @RequestBody @Schema(implementation = PaperWriteRequest.class) PaperWriteRequest paperWriteRequest
    );

    @Operation(summary = "차용증 상세 조회 API", description = "차용증 id를 파라미터로 입력하여 상세 조회가 가능합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 조회되었습니다.",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = PaperDetailResponse.class))
                    })
    })
    ResponseEntity<PaperDetailResponse> detail(@AuthenticationPrincipal LoginUser loginUser,
                                               @Parameter(description = "차용증 id", required = true) Long id);

    @Operation(summary = "차용증 목록 조회 API", description = "로그인한 회원의 정보를 기반으로 연관 차용증을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 조회되었습니다.",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = PaperListResponse.class))
                    })
    })
    ResponseEntity<List<PaperListResponse>> list(@AuthenticationPrincipal LoginUser loginUser);

    @Operation(summary = "차용증 승인 API", description = "작성 요청을 받은 차용증의 승인을 처리합니다.")
    @ApiErrorCodeExample(PromissoryPaperErrorCode.class)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "성공적으로 작성되었습니다."),
    })
    ResponseEntity<Void> acceptPaper(@AuthenticationPrincipal LoginUser loginUser,
                                     @Parameter(description = "차용증 id", required = true) Long id);

    @Operation(summary = "차용증 수정 요청 API", description = "차용증의 수정을 요청합니다.")
    @ApiErrorCodeExample(PromissoryPaperErrorCode.class)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "성공적으로 작성되었습니다."),
    })
    ResponseEntity<Void> requestModify(@AuthenticationPrincipal LoginUser loginUser,
                                       @RequestBody @Schema(implementation = PaperModifyRequest.class) PaperModifyRequest paperModifyRequest);

    @Operation(summary = "차용증 수정 승인 및 적용 API", description = "수정 요청을 받은 차용증의 수정을 진행합니다.")
    @ApiErrorCodeExample(PromissoryPaperErrorCode.class)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "성공적으로 작성되었습니다."),
    })
    ResponseEntity<Void> modifying(@AuthenticationPrincipal LoginUser loginUser,
                                   @Parameter(description = "차용증 id", required = true) Long id,
                                   @RequestBody @Schema(implementation = PaperWriteRequest.class) PaperWriteRequest paperWriteRequest);

    @Operation(summary = "일부 상환 내역기록 API", description = "채권자가 일부 상환 내용을 작성합니다.")
    @ApiErrorCodeExample(RepaymentErrorCode.class)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "성공적으로 작성되었습니다."),
    })
    ResponseEntity<Void> repaymentRequest(@AuthenticationPrincipal LoginUser loginUser,
                                          @RequestBody @Schema(implementation = RepaymentRequest.class) RepaymentRequest repaymentRequest);

    @Operation(summary = "일부 상환 내역 삭제 API", description = "채권자가 이전에 기록한 일부 상환 내역을 삭제합니다.")
    @ApiErrorCodeExample(RepaymentErrorCode.class)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "성공적으로 작성되었습니다."),
    })
    ResponseEntity<Void> repaymentCancel(@AuthenticationPrincipal LoginUser loginUser,
                                         @RequestBody @Schema(implementation = RepaymentCancelRequest.class) RepaymentCancelRequest repaymentCancelRequest);

}