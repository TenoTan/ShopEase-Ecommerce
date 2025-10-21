package com.example.ecommerce.security;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Converter
public class SafeAttributeEncryptor implements AttributeConverter<String, String> {

    private static final String ALGORITHM = "AES";
    private static final byte[] KEY = "MySecretKey12345".getBytes();

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return attribute;
        }

        try {
            SecretKeySpec secretKey = new SecretKeySpec(KEY, ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(attribute.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting data", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return dbData;
        }

        // Check if data looks encrypted (Base64 pattern)
        if (!isBase64(dbData)) {
            // Plain text (old data) - return as-is
            return dbData;
        }

        // Encrypted data - decrypt it
        try {
            SecretKeySpec secretKey = new SecretKeySpec(KEY, ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(dbData)));
        } catch (Exception e) {
            // If decryption fails, return as-is to avoid breaking old data
            return dbData;
        }
    }

    /**
     * Check if string is Base64 encoded (likely encrypted)
     */
    private boolean isBase64(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }

        // Base64 pattern: alphanumeric + / + = with length divisible by 4
        String base64Pattern = "^[A-Za-z0-9+/]*={0,2}$";

        if (!str.matches(base64Pattern) || str.length() % 4 != 0) {
            return false;
        }

        // Encrypted data is typically longer than plain text
        // Phone numbers are 10-15 chars, encrypted version is 20+ chars
        return str.length() > 20;
    }
}
