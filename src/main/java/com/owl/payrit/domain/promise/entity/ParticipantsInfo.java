package com.owl.payrit.domain.promise.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ParticipantsInfo {

    private String participantsName;

    private String participantsPhone;
}
