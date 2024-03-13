package com.owl.payrit.domain.auth.dto.response;

import com.owl.payrit.domain.auth.domain.OauthProvider;
import com.owl.payrit.domain.member.entity.Member;
import com.owl.payrit.domain.member.entity.OauthInformation;
import com.owl.payrit.domain.member.entity.Role;

public record AppleUser(
    String sub,
    String email
) {
    public Member toEntity() {
        return Member.builder()
                     .oauthInformation(new OauthInformation(sub, OauthProvider.APPLE))
                     .email(email)
                     .role(Role.MEMBER)
                     .build();
    }
}
