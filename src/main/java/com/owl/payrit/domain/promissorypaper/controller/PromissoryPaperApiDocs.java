package com.owl.payrit.domain.promissorypaper.controller;

import com.owl.payrit.domain.auth.dto.response.LoginUser;
import com.owl.payrit.domain.promissorypaper.dto.request.PaperModifyRequest;
import com.owl.payrit.domain.promissorypaper.dto.request.PaperWriteRequest;
import com.owl.payrit.domain.promissorypaper.dto.response.PaperDetailResponse;
import com.owl.payrit.domain.promissorypaper.dto.response.PaperListResponse;
import com.owl.payrit.domain.promissorypaper.exception.PromissoryPaperException;
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 작성되었습니다."),
            @ApiResponse(responseCode = "400", description = "차용증 데이터가 올바르지 않습니다.")
    })
    ResponseEntity<String> write(@AuthenticationPrincipal LoginUser loginUser,
                                 @RequestBody @Schema(implementation = PaperWriteRequest.class) PaperWriteRequest paperWriteRequest
    );

    @Operation(summary = "차용증 상세 조회 API", description = "차용증 id를 파라미터로 입력하여 상세 조회가 가능합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 조회되었습니다.",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = PaperDetailResponse.class))
                    }),
            @ApiResponse(responseCode = "403", description = "차용증 접근 권한이 없습니다."),
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 승인되었습니다."),
            @ApiResponse(responseCode = "400",
                    description = "1. 자신이 직접 승인하려는 경우\n"
                            + "2. 승인할 수 없는 단계에 있는 경우\n"
                            + "3. 차용증 정보와 로그인한 회원의 정보가 일치하지 않는 경우\n")
    })
    ResponseEntity<String> acceptPaper(@AuthenticationPrincipal LoginUser loginUser,
                                       @Parameter(description = "차용증 id", required = true) Long id);

    @Operation(summary = "차용증 수정 요청 API", description = "차용증의 수정을 요청합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 요청이 완료 되었습니다."),
            @ApiResponse(responseCode = "400", description = "승인 대기 단계가 아니기 때문에 요청이 불가합니다."),
            @ApiResponse(responseCode = "403", description = "차용증 접근 권한이 없습니다.")
    })
    ResponseEntity<String> requestModify(@AuthenticationPrincipal LoginUser loginUser,
                                         @RequestBody @Schema(implementation = PaperModifyRequest.class) PaperModifyRequest paperModifyRequest);
}