package com.owl.payrit.domain.memo.controller;

import com.owl.payrit.domain.auth.dto.response.LoginUser;
import com.owl.payrit.domain.memo.dto.request.MemoWriteRequest;
import com.owl.payrit.domain.memo.dto.response.MemoListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "메모 API", description = "메모 CRUD API 입니다.")
public interface MemoApiDocs {

    @Operation(summary = "차용증에 대한 메모 목록 조회 API", description = "사용자가 차용증에 작성한 메모 목록 조회입니다.")
    @ApiResponse(responseCode = "200", description = "메모 목록을 반환합니다.",
        content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = MemoListResponse.class))
        })
    ResponseEntity<List<MemoListResponse>> getMemoListByPaperId(
        @AuthenticationPrincipal LoginUser loginUser,
        @Parameter(description = "차용증 PK", required = true) Long paperId);

    @Operation(summary = "메모 작성 API", description = "메모 작성용 API 입니다. 차용증에 관련된 유저가 아닐 경우 401 에러를 반환합니다.")
    @ApiResponse(responseCode = "204")
    ResponseEntity<Void> createMemo(
        @AuthenticationPrincipal LoginUser loginUser,
        @Parameter(description = "차용증 PK", required = true) Long paperId,
        @RequestBody @Schema(implementation = MemoWriteRequest.class) MemoWriteRequest memoWriteRequest);

    @Operation(summary = "메모 수정 API", description = "메모 수정용 API 입니다. 차용증에 관련된 유저가 아닐 경우 401 에러를 반환합니다.")
    ResponseEntity<Void> modifyMemo(@AuthenticationPrincipal LoginUser loginUser,
        @Parameter(description = "메모 PK", required = true) Long memoId,
        @RequestBody @Schema(implementation = MemoWriteRequest.class) MemoWriteRequest memoWriteRequest);

    @Operation(summary = "메모 삭제 API", description = "메모 삭제 API 입니다. 차용증에 관련된 유저가 아닐 경우 401 에러를 반환합니다.")
    ResponseEntity<Void> deleteMemo(
        @AuthenticationPrincipal LoginUser loginUser,
        @Parameter(description = "메모 PK", required = true) Long memoId);
}

