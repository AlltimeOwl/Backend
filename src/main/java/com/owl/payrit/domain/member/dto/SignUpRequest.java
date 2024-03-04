package com.owl.payrit.domain.member.dto;

import java.time.LocalDate;

public record SignUpRequest(
    String email,
    String name,
    String phoneNumber,
    LocalDate birthDay,
    String address
) {

}
