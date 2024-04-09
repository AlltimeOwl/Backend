package com.owl.payrit.domain.auth.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.owl.payrit.domain.member.entity.CertificationInformation;

public record PortOneCertificationResponse(
    Integer code,
    String message,
    CertificationAnnotation response
) {

    public CertificationInformation toEntity() {
        return CertificationInformation.builder()
                                       .impUid(response.impUid)
                                       .name(response.name)
                                       .gender(response.gender)
                                       .birthday(response.birthday)
                                       .phone(response.phone)
                                       .build();
    }

    @JsonNaming(SnakeCaseStrategy.class)
    public record CertificationAnnotation(
        String impUid,
        String merchantUid,
        String pgTid,
        String pgProvider,
        String name,
        String gender,
        String birthday,
        boolean foreigner,
        String phone,
        String carrier,
        boolean certified,
        Integer certifiedAt,
        String uniqueKey,
        String uniqueInSite,
        String origin,
        String foreignerV2
    ) {

    }
}
