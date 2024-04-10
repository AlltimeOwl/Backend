package com.owl.payrit.domain.promissorypaper.entity;

import com.owl.payrit.global.encryption.PromissoryPaperStringConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class PaperProfile {

    @Convert(converter = PromissoryPaperStringConverter.class)
    private String name;

    @Convert(converter = PromissoryPaperStringConverter.class)
    private String phoneNumber;

    @Convert(converter = PromissoryPaperStringConverter.class)
    private String address;
}
