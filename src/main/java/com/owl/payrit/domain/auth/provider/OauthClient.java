package com.owl.payrit.domain.auth.provider;

import com.owl.payrit.domain.auth.domain.OauthProvider;
import com.owl.payrit.domain.auth.dto.request.LoginTokenRequest;
import com.owl.payrit.domain.member.entity.Member;

public interface OauthClient {

    OauthProvider oauthProvider();

    Member fetch(LoginTokenRequest loginTokenRequest);

    void revoke(String authorizationCode );
}
