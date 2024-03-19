package com.owl.payrit.domain.memo.service;

import com.owl.payrit.domain.auth.dto.response.LoginUser;
import com.owl.payrit.domain.memo.dto.response.MemoListResponse;
import com.owl.payrit.domain.memo.repository.MemoRepository;
import com.owl.payrit.domain.promissorypaper.entity.PromissoryPaper;
import com.owl.payrit.domain.promissorypaper.exception.PromissoryPaperErrorCode;
import com.owl.payrit.domain.promissorypaper.exception.PromissoryPaperException;
import com.owl.payrit.domain.promissorypaper.service.PromissoryPaperService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemoService {

    private final MemoRepository memoRepository;
    private final PromissoryPaperService promissoryPaperService;

    public List<MemoListResponse> findAllByPaperIdAndMemberId(LoginUser loginUser, Long paperId) {

        checkPermission(loginUser.id(), paperId);
        return memoRepository.findAllByMemberIdAndPromissoryPaperIdOrderByCreatedAt(loginUser.id(), paperId)
                             .stream()
                             .map(MemoListResponse::new)
                             .toList();
    }

    private void checkPermission(Long memberId, Long paperId) {
        PromissoryPaper promissoryPaper = promissoryPaperService.getById(paperId);
        boolean checkPermission = promissoryPaperService.isMine(memberId, promissoryPaper);
        if (!checkPermission) {
            throw new PromissoryPaperException(PromissoryPaperErrorCode.PAPER_IS_NOT_MINE);
        }
    }

}
