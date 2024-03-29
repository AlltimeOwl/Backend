package com.owl.payrit.global.testutil;

import com.owl.payrit.domain.auth.domain.OauthProvider;
import com.owl.payrit.domain.member.entity.Member;
import com.owl.payrit.domain.member.entity.OauthInformation;
import com.owl.payrit.domain.member.entity.Role;
import com.owl.payrit.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Profile({"local", "h2"})
@RequiredArgsConstructor
@Component
public class TestDataInitializer implements CommandLineRunner {

    private final MemberRepository memberRepository;

    @Override
    public void run(String... args) throws Exception {
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
