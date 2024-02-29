package com.owl.payrit.domain.member.dto;

import com.owl.payrit.domain.member.entity.Gender;
import java.time.LocalDate;

public record SignUpRequest(
    String email,
    String name,
    String phoneNumber,
    Gender gender,
    LocalDate birthDay,
    String address
) {

}
