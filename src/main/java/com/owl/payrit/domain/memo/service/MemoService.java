package com.owl.payrit.domain.memo.service;

import com.owl.payrit.domain.auth.dto.response.LoginUser;
import com.owl.payrit.domain.memo.dto.request.MemoWriteRequest;
import com.owl.payrit.domain.memo.dto.response.MemoListResponse;
import com.owl.payrit.domain.memo.entity.Memo;
import com.owl.payrit.domain.memo.exception.MemoErrorCode;
import com.owl.payrit.domain.memo.exception.MemoException;
import com.owl.payrit.domain.memo.repository.MemoRepository;
import com.owl.payrit.domain.promissorypaper.entity.PromissoryPaper;
import com.owl.payrit.domain.promissorypaper.exception.PromissoryPaperErrorCode;
import com.owl.payrit.domain.promissorypaper.exception.PromissoryPaperException;
import com.owl.payrit.domain.promissorypaper.service.PromissoryPaperService;
import java.util.List;
import java.util.Objects;
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

        checkPaperPermission(loginUser.id(), paperId);
        return memoRepository.findAllByMemberIdAndPromissoryPaperIdOrderByCreatedAt(loginUser.id(), paperId)
                             .stream()
                             .map(MemoListResponse::new)
                             .toList();
    }

    private void checkPaperPermission(Long memberId, Long paperId) {
        PromissoryPaper promissoryPaper = promissoryPaperService.getById(paperId);
        boolean checkPermission = promissoryPaperService.isMine(memberId, promissoryPaper);
        if (!checkPermission) {
            throw new PromissoryPaperException(PromissoryPaperErrorCode.PAPER_IS_NOT_MINE);
        }
    }

    private void checkMemoPermission(Long memberId, Long memoWriterId) {
        if(!Objects.equals(memberId, memoWriterId)) throw new MemoException(MemoErrorCode.UNAUTHORIZED_MODIFY_MEMO);
    }

    @Transactional
    public long write(LoginUser loginUser, Long paperId, MemoWriteRequest memoWriteRequest) {
        PromissoryPaper promissoryPaper = promissoryPaperService.getById(paperId);
        checkPaperPermission(loginUser.id(), paperId);
        Memo memo = memoWriteRequest.toEntity(promissoryPaper, loginUser.id());
        return memoRepository.save(memo).getId();
    }

    @Transactional
    public void modify(LoginUser loginUser, Long memoId, MemoWriteRequest memoWriteRequest) {
        Memo memo = findById(memoId);
        checkMemoPermission(loginUser.id(), memo.getMemberId());

        memo.update(memoWriteRequest.content());
    }

    private Memo findById(Long id) {
        return memoRepository.findById(id).orElseThrow(() -> new MemoException(MemoErrorCode.MEMO_NOT_FOUND));
    }
}
