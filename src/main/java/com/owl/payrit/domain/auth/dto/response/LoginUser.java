package com.owl.payrit.domain.auth.dto.response;

import com.owl.payrit.domain.member.entity.OauthInformation;

public record LoginUser(
    Long id,
    OauthInformation oauthInformation
) {

}
