package com.owl.payrit.domain.promissorypaper.service;

import com.owl.payrit.domain.auth.dto.response.LoginUser;
import com.owl.payrit.domain.member.entity.Member;
import com.owl.payrit.domain.member.entity.OauthInformation;
import com.owl.payrit.domain.member.service.MemberService;
import com.owl.payrit.domain.promissorypaper.dto.request.PaperWriteRequest;
import com.owl.payrit.domain.promissorypaper.entity.PaperRole;
import com.owl.payrit.domain.promissorypaper.entity.PromissoryPaper;
import com.owl.payrit.domain.promissorypaper.repository.PromissoryPaperRepository;
import com.owl.payrit.util.ServiceTest;
import org.apache.juli.logging.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static com.owl.payrit.domain.auth.domain.OauthProvider.FAKE_KAKAO;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class PromissoryPaperServiceTest extends ServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    PromissoryPaperService promissoryPaperService;

    PaperWriteRequest creditorWriteRequest;
    PaperWriteRequest debtorWriteRequest;

    private final static String TEST_CONTENT = "테스트용 내용입니다.";

    @BeforeEach
    void setting() {

        creditorWriteRequest = new PaperWriteRequest(
                PaperRole.CREDITOR, 3000, LocalDate.now(), LocalDate.now(),
                LocalDate.now().plusDays(7), TEST_CONTENT, 12, 10,
                "name00", "010-1234-5670", "(12345) 서울시 종로구 광화문로 1234",
                "name01", "010-1234-5671", "(67890) 경기도 고양시 일산서로 5678"
        );

        debtorWriteRequest = new PaperWriteRequest(
                PaperRole.DEBTOR, 5000, LocalDate.now(), LocalDate.now(),
                LocalDate.now().plusDays(7), TEST_CONTENT, 20, 5,
                "name01", "010-1234-5671", "(12345) 서울시 종로구 광화문로 1234",
                "name00", "010-1234-5670", "(67890) 경기도 고양시 일산서로 5678"
        );

        setUp();
    }

    private LoginUser prepareLoginUser() {
        Member member = findByEmail("test00");
        return new LoginUser(member.getId(), member.getOauthInformation());
    }

    @Test
    @DisplayName("차용증을 작성할 수 있다.")
    void t001() {

        LoginUser loginUser = prepareLoginUser();

        Long paperId = promissoryPaperService.writePaper(loginUser, creditorWriteRequest);

        PromissoryPaper paper = promissoryPaperService.getById(paperId);

        assertThat(paper.getSpecialConditions()).isEqualTo(TEST_CONTENT);
    }

    @Test
    @DisplayName("작성시, 빌려준 상황을 선택했다면, Creditor의 데이터에 회원의 정보가 입력되어야 함.")
    void t002() {

        LoginUser loginUser = prepareLoginUser();

        Long paperId = promissoryPaperService.writePaper(loginUser, creditorWriteRequest);

        PromissoryPaper paper = promissoryPaperService.getById(paperId);
        Member loginedMember = memberService.findById(loginUser.id());

        assertThat(paper.getCreditor()).isEqualTo(loginedMember);
        assertThat(paper.getCreditorPhoneNumber()).isEqualTo(loginedMember.getPhoneNumber());
    }

    @Test
    @DisplayName("작성시, 빌린 상황을 선택했다면, Debtor의 데이터에 회원의 정보가 입력되어야 함.")
    void t003() {

        LoginUser loginUser = prepareLoginUser();

        Long paperId = promissoryPaperService.writePaper(loginUser, debtorWriteRequest);

        PromissoryPaper paper = promissoryPaperService.getById(paperId);
        Member loginedMember = memberService.findById(loginUser.id());

        assertThat(paper.getDebtor()).isEqualTo(loginedMember);
        assertThat(paper.getDebtorPhoneNumber()).isEqualTo(loginedMember.getPhoneNumber());
    }

    @Test
    @DisplayName("작성시, 이자율은 0% 부터 20% 까지만 설정할 수 있음.")
    void t004() {

    }

    @Test
    @DisplayName("작성시, 상환 시작일은 과거로 설정할 수 없음.")
    void t005() {

    }

    @Test
    @DisplayName("작성시, 상환 마감일은 상환 시작일보다 과거일 수 없음.")
    void t006() {

    }

    @Test
    @DisplayName("상세 조회시, 채권자 혹은 채무자만 열람할 수 있음.")
    void t007() {

    }

    @Test
    @DisplayName("승인시, 승인 대기 단계에서만 승인이 가능함.")
    void t008() {

    }

    @Test
    @DisplayName("승인시, 작성자가 Creditor 였다면 회원은 Debtor의 데이터와 일치해야함.")
    void t009() {

    }

    @Test
    @DisplayName("승인시, 작성자가 Debtor 였다면 회원은 Creditor 데이터와 일치해야함.")
    void t010() {

    }

    @Test
    @DisplayName("승인시, 자신이 작성할 차용증을 자신이 승인할 수는 없음.")
    void t011() {

    }

    @Test
    @DisplayName("수정 요청시, 승인 대기 단계에서만 수정 요청이 가능함.")
    void t012() {

    }

    @Test
    @DisplayName("수정 요청시, 승인 요청을 받은 상대방만 수정 요청이 가능함.")
    void t013() {

    }

    @Test
    @DisplayName("수정 진행시, 수정을 요청받은 상태일 경우에만 수정이 가능함.")
    void t014() {

    }

    @Test
    @DisplayName("수정 진행시, 첫 작성자만 수정 진행이 가능함.")
    void t015() {

    }
}