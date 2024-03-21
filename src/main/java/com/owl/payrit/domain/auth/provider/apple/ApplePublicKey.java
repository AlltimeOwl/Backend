package com.owl.payrit.domain.auth.provider.apple;

public record ApplePublicKey(
    String kty,
    String kid,
    String use,
    String alg,
    String n,
    String e
) {

}


