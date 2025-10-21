package com.example.ecommerce.security;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * JPA attribute converter that automatically encrypts/decrypts entity attributes
 * Can be applied to entity fields that contain sensitive data
 * Handles both legacy plain text data and new encrypted data for backward compatibility
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

        // Check if already encrypted to avoid double encryption
        if (isLikelyEncrypted(attribute)) {
            return attribute;
        }

        return encryptionUtil.encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        // Check if data looks like plain text
        if (!isLikelyEncrypted(dbData)) {
            // Return plain text as-is for backward compatibility
            return dbData;
        }

        try {
            // Try to decrypt
            return encryptionUtil.decrypt(dbData);
        } catch (Exception e) {
            // If decryption fails, return original value (likely plain text)
            System.err.println("Warning: Failed to decrypt data, returning plain text: " + e.getMessage());
            return dbData;
        }
    }

    /**
     * Heuristic to detect if a string is likely encrypted (Base64 encoded)
     * AES-GCM encrypted data with IV is at least 20 characters and contains only Base64 characters
     *
     * @param value The string to check
     * @return true if the string appears to be encrypted, false otherwise
     */
    private boolean isLikelyEncrypted(String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }

        // Encrypted data is at least 20 characters (12-byte IV + ciphertext + 16-byte tag)
        // Base64 encoding makes it even longer
        if (value.length() < 20) {
            return false;
        }

        // Check if it contains only valid Base64 characters
        // Base64 alphabet: A-Z, a-z, 0-9, +, /, and = for padding
        return value.matches("^[A-Za-z0-9+/]+=*$");
    }
}
