package com.owl.payrit.domain.promissorypaper.service;

import com.owl.payrit.domain.auth.dto.response.LoginUser;
import com.owl.payrit.domain.member.entity.Member;
import com.owl.payrit.domain.member.service.MemberService;
import com.owl.payrit.domain.promissorypaper.dto.request.PaperModifyRequest;
import com.owl.payrit.domain.promissorypaper.dto.request.PaperWriteRequest;
import com.owl.payrit.domain.promissorypaper.dto.response.PaperDetailResponse;
import com.owl.payrit.domain.promissorypaper.entity.PaperRole;
import com.owl.payrit.domain.promissorypaper.entity.PromissoryPaper;
import com.owl.payrit.domain.promissorypaper.exception.PromissoryPaperException;
import com.owl.payrit.domain.promissorypaper.repository.PromissoryPaperRepository;
import com.owl.payrit.util.ServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class PromissoryPaperServiceTest extends ServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    PromissoryPaperService promissoryPaperService;

    @Autowired
    PromissoryPaperRepository promissoryPaperRepository;
    PaperWriteRequest creditorWriteRequest;
    PaperWriteRequest debtorWriteRequest;
    PaperWriteRequest modifyWriteRequest;

    MockHttpServletRequest request = new MockHttpServletRequest();

    private final static String TEST_CONTENT = "테스트용 내용입니다.";
    private final static String WRITER_CREDITOR_EMAIL = "test00";
    private final static String PEER_DEBTOR_EMAIL = "test01";
    private final static String OTHER_USER_EMAIL = "test08";

    @BeforeEach
    void setting() {

        creditorWriteRequest = new PaperWriteRequest(
            PaperRole.CREDITOR, 30000, 3000, LocalDate.now(), LocalDate.now(),
            LocalDate.now().plusDays(7), TEST_CONTENT, 12, 10,
            "name00", "010-1234-5670", "(12345) 서울시 종로구 광화문로 1234",
            "name01", "010-1234-5671", "(67890) 경기도 고양시 일산서로 5678"
        );

        debtorWriteRequest = new PaperWriteRequest(
            PaperRole.DEBTOR, 50000, 5000, LocalDate.now(), LocalDate.now(),
            LocalDate.now().plusDays(7), TEST_CONTENT, 20, 5,
            "name01", "010-1234-5671", "(12345) 서울시 종로구 광화문로 1234",
            "name00", "010-1234-5670", "(67890) 경기도 고양시 일산서로 5678"
        );

        modifyWriteRequest = new PaperWriteRequest(
            PaperRole.CREDITOR, 30000, 3000, LocalDate.now(), LocalDate.now(),
            LocalDate.now().plusDays(7), TEST_CONTENT.concat("(수정 완료)"), 12, 10,
            "name00", "010-1234-5670", "(12345) 서울시 종로구 광화문로 1234",
            "name01", "010-1234-5671", "(67890) 경기도 고양시 일산서로 5678"
        );

        setUp();
    }

    private LoginUser prepareLoginUserByEmail(String email) {
        Member member = findByEmail(email);
        return new LoginUser(member.getId(), member.getOauthInformation());
    }

    @Test
    @DisplayName("차용증을 작성할 수 있다.")
    void t001() throws IOException {

        LoginUser loginUser = prepareLoginUserByEmail(WRITER_CREDITOR_EMAIL);

        Long paperId = promissoryPaperService.writePaper(loginUser, creditorWriteRequest, request);

        PromissoryPaper paper = promissoryPaperService.getById(paperId);

        assertThat(paper.getSpecialConditions()).isEqualTo(TEST_CONTENT);
    }

    @Test
    @DisplayName("작성시, 빌려준 상황을 선택했다면, Creditor의 데이터에 회원의 정보가 입력되어야 함.")
    void t002() throws IOException {

        LoginUser loginUser = prepareLoginUserByEmail(WRITER_CREDITOR_EMAIL);

        Long paperId = promissoryPaperService.writePaper(loginUser, creditorWriteRequest, request);

        PromissoryPaper paper = promissoryPaperService.getById(paperId);
        Member loginedMember = memberService.findById(loginUser.id());

        assertThat(paper.getCreditor()).isEqualTo(loginedMember);
        assertThat(paper.getCreditorPhoneNumber()).isEqualTo(loginedMember.getPhoneNumber());
    }

    @Test
    @DisplayName("작성시, 빌린 상황을 선택했다면, Debtor의 데이터에 회원의 정보가 입력되어야 함.")
    void t003() throws IOException {

        LoginUser loginUser = prepareLoginUserByEmail(WRITER_CREDITOR_EMAIL);

        Long paperId = promissoryPaperService.writePaper(loginUser, debtorWriteRequest, request);

        PromissoryPaper paper = promissoryPaperService.getById(paperId);
        Member loginedMember = memberService.findById(loginUser.id());

        assertThat(paper.getDebtor()).isEqualTo(loginedMember);
        assertThat(paper.getDebtorPhoneNumber()).isEqualTo(loginedMember.getPhoneNumber());
    }

    @Test
    @DisplayName("작성시, 이자율은 20%를 초과할 수 없다.")
    void t004() {

        LoginUser loginUser = prepareLoginUserByEmail(WRITER_CREDITOR_EMAIL);

        PaperWriteRequest paperWriteRequest = new PaperWriteRequest(
                PaperRole.DEBTOR, 5000, 500, LocalDate.now(), LocalDate.now(),
                LocalDate.now().plusDays(7), TEST_CONTENT, 30, 5,
                "name01", "010-1234-5671", "(12345) 서울시 종로구 광화문로 1234",
                "name00", "010-1234-5670", "(67890) 경기도 고양시 일산서로 5678"
        );

        assertThrows(PromissoryPaperException.class, () -> {
            promissoryPaperService.writePaper(loginUser, paperWriteRequest, request);
        });
    }

    @Test
    @DisplayName("작성시, 상환 시작일은 과거로 설정할 수 없음.")
    void t005() {

        LoginUser loginUser = prepareLoginUserByEmail(WRITER_CREDITOR_EMAIL);

        PaperWriteRequest paperWriteRequest = new PaperWriteRequest(
                PaperRole.DEBTOR, 5000, 500, LocalDate.now(), LocalDate.now().minusDays(3),
                LocalDate.now().plusDays(7), TEST_CONTENT, 30, 5,
                "name01", "010-1234-5671", "(12345) 서울시 종로구 광화문로 1234",
                "name00", "010-1234-5670", "(67890) 경기도 고양시 일산서로 5678"
        );


        assertThrows(PromissoryPaperException.class, () -> {
            promissoryPaperService.writePaper(loginUser, paperWriteRequest, request);
        });
    }

    @Test
    @DisplayName("작성시, 상환 마감일은 상환 시작일보다 과거일 수 없음.")
    void t006() {

        LoginUser loginUser = prepareLoginUserByEmail(WRITER_CREDITOR_EMAIL);

        PaperWriteRequest paperWriteRequest = new PaperWriteRequest(
                PaperRole.DEBTOR, 5000, 500, LocalDate.now(), LocalDate.now(),
                LocalDate.now().minusDays(3), TEST_CONTENT, 30, 5,
                "name01", "010-1234-5671", "(12345) 서울시 종로구 광화문로 1234",
                "name00", "010-1234-5670", "(67890) 경기도 고양시 일산서로 5678"
        );


        assertThrows(PromissoryPaperException.class, () -> {
            promissoryPaperService.writePaper(loginUser, paperWriteRequest, request);
        });
    }

    @Test
    @DisplayName("상세 조회시, 채권자 혹은 채무자만 열람할 수 있음")
    void t007() throws IOException {

        LoginUser loginUser = prepareLoginUserByEmail(WRITER_CREDITOR_EMAIL);
        LoginUser otherUser = prepareLoginUserByEmail(OTHER_USER_EMAIL);

        Long paperId = promissoryPaperService.writePaper(loginUser, debtorWriteRequest, request);

        PaperDetailResponse detail = promissoryPaperService.getDetail(loginUser, paperId);

        assertThat(detail.specialConditions()).isEqualTo(TEST_CONTENT);

        assertDoesNotThrow(() -> {
            promissoryPaperService.getDetail(loginUser, paperId);
        });

        assertThrows(PromissoryPaperException.class, () -> {
            promissoryPaperService.getDetail(otherUser, paperId);
        });
    }

//    @Test
//    @DisplayName("승인시, 승인 대기 단계에서만 승인이 가능함.")
//    void t008() throws IOException {
//
//        LoginUser creditorUser = prepareLoginUserByEmail(WRITER_CREDITOR_EMAIL);
//        LoginUser debtorUser = prepareLoginUserByEmail(PEER_DEBTOR_EMAIL);
//
//        Long paperId = promissoryPaperService.writePaper(creditorUser, creditorWriteRequest, request);
//
//        //승인 대기 상태이기 떄문에 승인이 가능
//        assertDoesNotThrow(() -> {
//            promissoryPaperService.acceptPaper(debtorUser, paperId);
//        });
//
//        //승인 완료 상태이기 때문에 승인이 불가능
//        assertThrows(PromissoryPaperException.class, () -> {
//            promissoryPaperService.acceptPaper(debtorUser, paperId);
//        });
//    }

    @Test
    @DisplayName("승인시, 작성자가 Creditor 였다면 Debtor, Debtor 였다면 Creditor 의 데이터와 일치해야함.")
    void t009() throws IOException {

        LoginUser requesteUser = prepareLoginUserByEmail(WRITER_CREDITOR_EMAIL);
        LoginUser accepteUser = prepareLoginUserByEmail(PEER_DEBTOR_EMAIL);

        Member accepter = memberService.findById(accepteUser.id());

        Long paperId = promissoryPaperService.writePaper(requesteUser, creditorWriteRequest, request);
        PromissoryPaper paper = promissoryPaperService.getById(paperId);


        if (paper.getWriterRole().equals(PaperRole.CREDITOR)) {
            assertThat(accepter).isEqualTo(paper.getDebtor());
            assertThat(accepter.getPhoneNumber()).isEqualTo(paper.getDebtorPhoneNumber());
        } else if (paper.getWriterRole().equals(PaperRole.DEBTOR)) {
            assertThat(accepter).isEqualTo(paper.getCreditor());
            assertThat(accepter.getPhoneNumber()).isEqualTo(paper.getCreditorPhoneNumber());
        }
    }

//    @Test
//    @DisplayName("승인시, 자신이 작성할 차용증을 자신이 승인할 수는 없음.")
//    void t011() {
//
//        LoginUser creditorUser = prepareLoginUserByEmail(WRITER_CREDITOR_EMAIL);
//
//        Long paperId = promissoryPaperService.writePaper(creditorUser, creditorWriteRequest);
//
//        //작성자와 승인자가 일치할 수 없음.
//        assertThrows(PromissoryPaperException.class, () -> {
//            promissoryPaperService.acceptPaper(creditorUser, paperId);
//        });
//    }

//    @Test
//    @DisplayName("승인시, 외부인이 차용증을 승인할 수는 없음.")
//    void t012() {
//
//        LoginUser creditorUser = prepareLoginUserByEmail(WRITER_CREDITOR_EMAIL);
//        LoginUser otherUser = prepareLoginUserByEmail(OTHER_USER_EMAIL);
//
//        Long paperId = promissoryPaperService.writePaper(creditorUser, creditorWriteRequest);
//
//        assertThrows(PromissoryPaperException.class, () -> {
//            promissoryPaperService.acceptPaper(otherUser, paperId);
//        });
//    }

    @Test
    @DisplayName("수정 요청시, 승인 대기 단계에서만 수정 요청이 가능함.")
    void t013() throws IOException {

        LoginUser creditorUser = prepareLoginUserByEmail(WRITER_CREDITOR_EMAIL);
        LoginUser debtorUser = prepareLoginUserByEmail(PEER_DEBTOR_EMAIL);

        Long paperId = promissoryPaperService.writePaper(creditorUser, creditorWriteRequest, request);

        PaperModifyRequest modifyRequest = new PaperModifyRequest(paperId, "테스트용 수정 요청");

        //승인 대기 상태이기 떄문에 수정 요청이 가능
        assertDoesNotThrow(() -> {
            promissoryPaperService.sendModifyRequest(debtorUser, modifyRequest);
        });

        //수정 요청이 이미 보내져 상태가 변경되었기 떄문에 수정 요청이 불가
        assertThrows(PromissoryPaperException.class, () -> {
            promissoryPaperService.sendModifyRequest(debtorUser, modifyRequest);
        });
    }

    @Test
    @DisplayName("수정 요청시, 승인 요청을 받은 상대방만 수정 요청이 가능함.")
    void t014() throws IOException {

        LoginUser writerUser = prepareLoginUserByEmail(WRITER_CREDITOR_EMAIL);
        LoginUser peerUser = prepareLoginUserByEmail(PEER_DEBTOR_EMAIL);
        LoginUser otherUser = prepareLoginUserByEmail(OTHER_USER_EMAIL);

        Long paperId = promissoryPaperService.writePaper(writerUser, creditorWriteRequest, request);
        PaperModifyRequest modifyRequest = new PaperModifyRequest(paperId, "테스트용 수정 요청");

        //외부인은 수정 요청이 불가
        assertThrows(PromissoryPaperException.class, () -> {
            promissoryPaperService.sendModifyRequest(otherUser, modifyRequest);
        });

        //상대방만 수정 요청이 가능
        assertDoesNotThrow(() -> {
            promissoryPaperService.sendModifyRequest(peerUser, modifyRequest);
        });
    }

    @Test
    @DisplayName("수정 진행시, 수정을 요청받은 상태일 경우에만 수정이 가능함.")
    void t015() throws IOException {

        LoginUser writerUser = prepareLoginUserByEmail(WRITER_CREDITOR_EMAIL);
        LoginUser peerUser = prepareLoginUserByEmail(PEER_DEBTOR_EMAIL);

        Long paperId = promissoryPaperService.writePaper(writerUser, creditorWriteRequest, request);
        PromissoryPaper paper = promissoryPaperService.getById(paperId);

        //수정 요청을 아직 받지 않아 수정이 불가능
        assertThrows(PromissoryPaperException.class, () -> {
            promissoryPaperService.modifyingPaper(writerUser, paperId, creditorWriteRequest);
        });

        assertThat(paper.getSpecialConditions().contains("(수정 완료)")).isFalse();

        //수정 요청을 보냄
        PaperModifyRequest modifyRequest = new PaperModifyRequest(paperId, "테스트용 수정 요청");
        promissoryPaperService.sendModifyRequest(peerUser, modifyRequest);

        //수정 요청을 받은 후이기에 수정이 가능
        assertDoesNotThrow(() -> {
            promissoryPaperService.modifyingPaper(writerUser, paperId, modifyWriteRequest);
        });

        assertThat(paper.getSpecialConditions().contains("(수정 완료)")).isTrue();
    }

    @Test
    @DisplayName("수정 진행시, 첫 작성자만 수정 진행이 가능함.")
    void t016() throws IOException {

        LoginUser writerUser = prepareLoginUserByEmail(WRITER_CREDITOR_EMAIL);
        LoginUser peerUser = prepareLoginUserByEmail(PEER_DEBTOR_EMAIL);

        Long paperId = promissoryPaperService.writePaper(writerUser, creditorWriteRequest, request);
        PromissoryPaper paper = promissoryPaperService.getById(paperId);

        PaperModifyRequest modifyRequest = new PaperModifyRequest(paperId, "테스트용 수정 요청");
        promissoryPaperService.sendModifyRequest(peerUser, modifyRequest);

        //상대방은 첫 작성자가 아니기 때문에 수정이 불가능
        assertThrows(PromissoryPaperException.class, () -> {
            promissoryPaperService.modifyingPaper(peerUser, paperId, creditorWriteRequest);
        });

        //첫 작성자는 수정이 가능
        assertDoesNotThrow(() -> {
            promissoryPaperService.modifyingPaper(writerUser, paperId, modifyWriteRequest);
        });

        assertThat(paper.getSpecialConditions().contains("(수정 완료)")).isTrue();
    }

    @Test
    @DisplayName("모든 연관관계 해제된다")
    void t017 () throws Exception {
        Member member = findByEmail("test00");

        LoginUser loginUser = prepareLoginUserByEmail(WRITER_CREDITOR_EMAIL);

        promissoryPaperService.writePaper(loginUser, creditorWriteRequest, request);
        promissoryPaperService.writePaper(loginUser, debtorWriteRequest, request);

        List<PromissoryPaper> promissoryPaperList = promissoryPaperRepository.findAllByCreditorOrDebtorOrWriter(member);

        assertThat(promissoryPaperList.size()).isEqualTo(2);

        promissoryPaperService.removeRelation(member);
        List<PromissoryPaper> removeList = promissoryPaperRepository.findAllByCreditorOrDebtorOrWriter(member);

        assertThat(promissoryPaperList.size()).isEqualTo(2);
        assertThat(removeList.size()).isEqualTo(0);
    }
}