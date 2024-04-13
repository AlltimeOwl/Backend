package com.owl.payrit.global.testutil;

import com.owl.payrit.domain.auth.domain.OauthProvider;
import com.owl.payrit.domain.member.entity.CertificationInformation;
import com.owl.payrit.domain.member.entity.Member;
import com.owl.payrit.domain.member.entity.OauthInformation;
import com.owl.payrit.domain.member.entity.Role;
import com.owl.payrit.domain.member.repository.MemberRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

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
        OauthInformation oauthInformation = new OauthInformation(String.valueOf(index), provider, null);
        String email = "test0%s".formatted(index);
        String name = "name0%s".formatted(index);
        return Member.builder()
                .oauthInformation(oauthInformation)
                .email(email)
                .phoneNumber("+82 10-1234-567%s".formatted(index))
                .birthDay(LocalDate.now())
                .name(name)
                .role(Role.MEMBER)
                .isAuthenticated(true)
                .certificationInformation(genCertInfo(index))
                .build();
    }

    private CertificationInformation genCertInfo(int index) {

        return CertificationInformation.builder()
                .impUid("CI" + index)
                .birthday("2000-01-01")
                .gender("male")
                .phone("0101234567%s".formatted(index))
                .name("test0%s".formatted(index))
                .build();
    }
}
