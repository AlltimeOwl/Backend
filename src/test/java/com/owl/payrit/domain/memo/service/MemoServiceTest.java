package com.owl.payrit.domain.memo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.owl.payrit.domain.auth.dto.response.LoginUser;
import com.owl.payrit.domain.member.entity.Member;
import com.owl.payrit.domain.memo.dto.request.MemoWriteRequest;
import com.owl.payrit.domain.memo.dto.response.MemoListResponse;
import com.owl.payrit.domain.memo.entity.Memo;
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

    @Test
    @Transactional
    @DisplayName("메모 작성된다")
    void memoShouldBeWritten() throws Exception {
        // Given
        Member member1 = findByEmail("test00");
        LoginUser loginUser = new LoginUser(member1.getId(), member1.getOauthInformation());

        PaperWriteRequest paperWriteRequest = new PaperWriteRequest(PaperRole.CREDITOR, 3000000,
            LocalDate.now(), LocalDate.now(), LocalDate.now(), "내용", 20.0f, 28, "name00",
            "010-1234-5670", "광화문", "name01", "010-1234-5671", "중구");

        Long paperId = promissoryPaperService.writePaper(loginUser, paperWriteRequest);

        // When
        MemoWriteRequest memoWriteRequest = new MemoWriteRequest("내용");
        Long memoId = memoService.write(loginUser, paperId, memoWriteRequest);

        // Then
        Memo memo = memoRepository.findById(memoId).orElse(null);
        assertNotNull(memo);
        assertEquals("내용", memo.getContent());
    }

    @Test
    @Transactional
    @DisplayName("메모목록 조회된다.")
    void memoListShouldBeRetrieved() throws Exception {
        // Given
        Member member1 = findByEmail("test00");
        LoginUser loginUser = new LoginUser(member1.getId(), member1.getOauthInformation());

        PaperWriteRequest paperWriteRequest = new PaperWriteRequest(PaperRole.CREDITOR, 3000000,
            LocalDate.now(), LocalDate.now(), LocalDate.now(), "내용", 20.0f, 28, "name00",
            "010-1234-5670", "광화문", "name01", "010-1234-5671", "중구");

        Long paperId = promissoryPaperService.writePaper(loginUser, paperWriteRequest);

        // When
        MemoWriteRequest memoWriteRequest = new MemoWriteRequest("내용");
        Long memoId = memoService.write(loginUser, paperId, memoWriteRequest);
        Long memoId2 = memoService.write(loginUser, paperId, memoWriteRequest);
        Long memoId3 = memoService.write(loginUser, paperId, memoWriteRequest);

        // Then
        List<MemoListResponse> memoList = memoService.findAllByPaperIdAndMemberId(loginUser, paperId);
        assertThat(memoList).satisfies(
            list -> assertThat(list.size()).isEqualTo(3),
            list -> assertThat(list.get(0).content()).isEqualTo("내용")
        );
    }
}