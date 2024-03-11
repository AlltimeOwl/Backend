package com.owl.payrit.domain.auth.provider.apple;

public record ApplePublicKey(
    String kty,
    String kid,
    String alg,
    String use,
    String n,
    String e
) {

}

