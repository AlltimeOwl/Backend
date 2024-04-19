package com.owl.payrit.domain.promise.controller;

import com.owl.payrit.domain.auth.dto.response.LoginUser;
import com.owl.payrit.domain.promise.dto.request.PromiseWriteRequest;
import com.owl.payrit.domain.promise.dto.response.PromiseDetailResponse;
import com.owl.payrit.domain.promise.dto.response.PromiseListResponse;
import com.owl.payrit.domain.promise.exception.PromiseErrorCode;
import com.owl.payrit.global.swagger.annotation.ApiErrorCodeExample;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "약속 관련 API", description = "약속 서비스 API 입니다.")
public interface PromiseApiDocs {

    @Operation(summary = "약속 작성 API", description = "약속 관련 데이터를 입력해 request 하면, 약속 작성이 완료됩니다.")
    @ApiErrorCodeExample(PromiseErrorCode.class)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "성공적으로 작성되었습니다."),
    })
    ResponseEntity<Void> write(@AuthenticationPrincipal LoginUser loginUser,
                               @RequestBody @Schema(implementation = PromiseWriteRequest.class) PromiseWriteRequest promiseWriteRequest);

    @Operation(summary = "약속 리스트 조회 API", description = "로그인한 사용자의 약속 리스트를 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "약속 리스트 조회에 성공하였습니다.",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = PromiseListResponse.class))
                    }),
    })
    ResponseEntity<List<PromiseListResponse>> list(@AuthenticationPrincipal LoginUser loginUser);

    @Operation(summary = "약속 상세 조회 API", description = "약속의 상세조회 내용을 가져옵니다.")
    @ApiErrorCodeExample(PromiseErrorCode.class)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "약속의 상세조회가 성공하였습니다.",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = PromiseDetailResponse.class))
                    }),
    })
    ResponseEntity<PromiseDetailResponse> detail(@AuthenticationPrincipal LoginUser loginUser,
                                @PathVariable(name = "id") Long paperId);

    @Operation(summary = "약속 삭제 API", description = "약속을 삭제합니다.")
    @ApiErrorCodeExample(PromiseErrorCode.class)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "성공적으로 삭제되었습니다."),
    })
    ResponseEntity<Void> remove(@AuthenticationPrincipal LoginUser loginUser,
                                @PathVariable(name = "id") Long paperId);
}
