package com.example.ecommerce.security;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * JPA attribute converter that automatically encrypts/decrypts entity attributes
 * Can be applied to entity fields that contain sensitive data
 */
@Converter
@Component
public class AttributeEncryptor implements AttributeConverter<String, String> {

    private final AESEncryptionUtil encryptionUtil;

    @Autowired
    public AttributeEncryptor(AESEncryptionUtil encryptionUtil) {
        this.encryptionUtil = encryptionUtil;
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) {
            return null;
        }
        return encryptionUtil.encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return encryptionUtil.decrypt(dbData);
    }
}