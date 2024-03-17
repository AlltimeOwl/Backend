package com.owl.payrit.global.encryption;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Optional;

@Converter
public class PromissoryPaperStringConverter implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return Optional.ofNullable(attribute)
                       .map(PromissoryPaperEncryptionHelper::encrypt)
                       .orElse(null);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return Optional.ofNullable(dbData)
                       .map(PromissoryPaperEncryptionHelper::decrypt)
                       .orElse(null);
    }
}
