package com.owl.payrit.domain.member.dto.request;

import java.time.LocalDate;

public record SignUpRequest(
    String email,
    String name,
    String phoneNumber,
    LocalDate birthDay,
    String address
) {

}
