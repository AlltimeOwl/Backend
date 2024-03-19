package com.owl.payrit.domain.memo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.owl.payrit.domain.auth.dto.response.LoginUser;
import com.owl.payrit.domain.member.entity.Member;
import com.owl.payrit.domain.memo.dto.request.MemoWriteRequest;
import com.owl.payrit.domain.memo.dto.response.MemoListResponse;
import com.owl.payrit.domain.memo.entity.Memo;
import com.owl.payrit.domain.memo.exception.MemoException;
import com.owl.payrit.domain.memo.repository.MemoRepository;
import com.owl.payrit.domain.promissorypaper.dto.request.PaperWriteRequest;
import com.owl.payrit.domain.promissorypaper.entity.PaperRole;
import com.owl.payrit.domain.promissorypaper.repository.PromissoryPaperRepository;
import com.owl.payrit.domain.promissorypaper.service.PromissoryPaperService;
import com.owl.payrit.util.ServiceTest;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

class MemoServiceTest extends ServiceTest {

    @Autowired
    PromissoryPaperService promissoryPaperService;

    @Autowired
    PromissoryPaperRepository promissoryPaperRepository;

    @Autowired
    MemoService memoService;

    @Autowired
    MemoRepository memoRepository;

    @BeforeEach
    void settingMember() {
        setUp();
    }

    // 공통 메소드

    private LoginUser prepareLoginUser() {
        Member member = findByEmail("test00");
        return new LoginUser(member.getId(), member.getOauthInformation());
    }

    private Long preparePromissoryPaper(LoginUser loginUser) {
        PaperWriteRequest paperWriteRequest = new PaperWriteRequest(PaperRole.CREDITOR, 3000000,
            LocalDate.now(), LocalDate.now(), LocalDate.now(), "내용", 20.0f, 28, "name00",
            "010-1234-5670", "광화문", "name01", "010-1234-5671", "중구");
        return promissoryPaperService.writePaper(loginUser, paperWriteRequest);
    }

    @Test
    @Transactional
    @DisplayName("메모가 작성되어야 함")
    void memoShouldBeWritten() throws Exception {
        // Given
        LoginUser loginUser = prepareLoginUser();
        Long paperId = preparePromissoryPaper(loginUser);

        // When
        Long memoId = memoService.write(loginUser, paperId, new MemoWriteRequest("내용"));

        // Then
        Memo memo = memoRepository.findById(memoId).orElse(null);
        assertNotNull(memo);
        assertEquals("내용", memo.getContent());
    }

    @Test
    @Transactional
    @DisplayName("메모 목록이 올바르게 조회되어야 함")
    void memoListShouldBeRetrieved() throws Exception {
        // Given
        LoginUser loginUser = prepareLoginUser();
        Long paperId = preparePromissoryPaper(loginUser);

        // When
        memoService.write(loginUser, paperId, new MemoWriteRequest("내용1"));
        memoService.write(loginUser, paperId, new MemoWriteRequest("내용2"));
        memoService.write(loginUser, paperId, new MemoWriteRequest("내용3"));

        // Then
        List<MemoListResponse> memoList = memoService.findAllByPaperIdAndMemberId(loginUser, paperId);
        assertThat(memoList).hasSize(3);
        assertThat(memoList.get(0).content()).isEqualTo("내용1");
    }

    @Test
    @Transactional
    @DisplayName("메모가 수정되어야 함")
    void memoShouldBeModified() throws Exception {
        // Given
        LoginUser loginUser = prepareLoginUser();
        Long paperId = preparePromissoryPaper(loginUser);
        Long memoId = memoService.write(loginUser, paperId, new MemoWriteRequest("내용"));

        // When
        memoService.modify(loginUser, memoId, new MemoWriteRequest("수정"));

        // Then
        Memo memo = memoRepository.findById(memoId).orElse(null);
        assertNotNull(memo);
        assertEquals("수정", memo.getContent());
    }

    @Test
    @Transactional
    @DisplayName("권한이 없을 시 MemoException 발생")
    void unauthorizedUserShouldThrowMemoException() throws Exception {
        // Given
        LoginUser loginUser = prepareLoginUser();
        Long paperId = preparePromissoryPaper(loginUser);
        Long memoId = memoService.write(loginUser, paperId, new MemoWriteRequest("내용"));

        // When
        LoginUser unauthorizedUser = new LoginUser(3L, loginUser.oauthInformation());

        // Then
        assertThatExceptionOfType(MemoException.class)
            .isThrownBy(() -> memoService.modify(unauthorizedUser, memoId, new MemoWriteRequest("수정")));
    }
}