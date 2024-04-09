package com.owl.payrit.domain.member.dto.response;

import com.owl.payrit.domain.member.entity.Member;

public record CertificationResponse(
    String certificationName,
    String certificationGender,
    String certificationBirthday,
    String certificationPhoneNumber
) {

    public static CertificationResponse of(Member member) {
        return new CertificationResponse(member.getCertificationInformation().getName(),
                                        member.getCertificationInformation().getGender(),
                                        member.getCertificationInformation().getBirthday(),
                                        member.getCertificationInformation().getPhone());
    }

}
