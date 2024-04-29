package com.owl.payrit.domain.promise.entity;

import com.owl.payrit.global.encryption.PromissoryPaperStringConverter;
import jakarta.persistence.Convert;
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

    @Convert(converter = PromissoryPaperStringConverter.class)
    private String participantsPhone;
}
