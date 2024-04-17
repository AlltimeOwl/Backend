package com.owl.payrit.domain.member.dto.response;

import com.owl.payrit.domain.member.entity.Member;

public record StatusResponse(
    boolean isAgreeNotification
) {
    public static StatusResponse of(Member member) {
        return new StatusResponse(member.isAgreeNotification());
    }
}
