package com.owl.payrit.util;

import com.owl.payrit.domain.auth.domain.OauthProvider;
import com.owl.payrit.domain.member.entity.Member;
import com.owl.payrit.domain.member.entity.OauthInformation;
import com.owl.payrit.domain.member.entity.Role;
import com.owl.payrit.domain.member.repository.MemberRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
public class ServiceTest{

    @Autowired MemberRepository memberRepository;

    @BeforeEach
    void setUp() throws Exception {
        List<Member> testMemberList = getTestMembers();
        memberRepository.saveAll(testMemberList);
    }

    private List<Member> getTestMembers() {
        return IntStream.range(0, 10)
                        .mapToObj(this::createTestMember)
                        .collect(Collectors.toList());
    }

    private Member createTestMember(int index) {
        OauthProvider provider = (index % 2 == 0) ? OauthProvider.KAKAO : OauthProvider.APPLE;
        OauthInformation oauthInformation = new OauthInformation(String.valueOf(index), provider);
        String email = "test0%s".formatted(index);
        String name = "name0%s".formatted(index);
        return Member.builder()
                     .oauthInformation(oauthInformation)
                     .email(email)
                     .phoneNumber("010-1234-567%s".formatted(index))
                     .birthDay(LocalDate.now())
                     .name(name)
                     .role(Role.MEMBER)
                     .build();
    }
}
