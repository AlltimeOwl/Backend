package com.owl.payrit.domain.auth.provider;

import com.owl.payrit.domain.auth.domain.OauthProvider;
import com.owl.payrit.domain.member.entity.Member;

public interface OauthClient {

    OauthProvider oauthProvider();

    Member fetch(String accessToken);

    void revoke(String authorizationCode );
}
