package com.owl.payrit.domain.memo.controller;

import com.owl.payrit.domain.auth.dto.response.LoginUser;
import com.owl.payrit.domain.memo.dto.response.MemoListResponse;
import com.owl.payrit.domain.memo.service.MemoService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
}
