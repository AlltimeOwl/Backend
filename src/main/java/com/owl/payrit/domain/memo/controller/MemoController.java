package com.owl.payrit.domain.memo.controller;

import com.owl.payrit.domain.auth.dto.response.LoginUser;
import com.owl.payrit.domain.memo.dto.request.MemoWriteRequest;
import com.owl.payrit.domain.memo.dto.response.MemoListResponse;
import com.owl.payrit.domain.memo.service.MemoService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/memo")
@RestController
public class MemoController {

    private final MemoService memoService;

    //TODO : 메모 작성, 메모 조회, 메모 수정?
    @GetMapping("/{paperId}")
    public ResponseEntity<List<MemoListResponse>> getMemoListByPaperId(@AuthenticationPrincipal LoginUser loginUser, @PathVariable Long paperId) {
        log.info("'{}' member requests memos on '{} paper'", loginUser.id(), paperId);
        List<MemoListResponse> memoList = memoService.findAllByPaperIdAndMemberId(loginUser, paperId);
        return ResponseEntity.ok().body(memoList);
    }

    @PostMapping("/{paperId}")
    public ResponseEntity<Void> createMemo(@AuthenticationPrincipal LoginUser loginUser, @PathVariable Long paperId, MemoWriteRequest memoWriteRequest) {
        log.info("'{}' member requests create memo", loginUser.id());
        memoService.write(loginUser, paperId, memoWriteRequest);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{memoId}")
    public ResponseEntity<Void> modifyMemo(@AuthenticationPrincipal LoginUser loginUser, @PathVariable Long memoId, MemoWriteRequest memoWriteRequest) {
        log.info("'{}' member requests modify memo : {}", loginUser.id(), memoId);
        memoService.modify(loginUser, memoId, memoWriteRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{memoId}")
    public ResponseEntity<Void> deleteMemo(@AuthenticationPrincipal LoginUser loginUser, @PathVariable Long memoId) {
        log.info("'{}' member requests delete memo : {}", loginUser.id(), memoId);
        memoService.delete(loginUser, memoId);
        return ResponseEntity.noContent().build();
    }
}
