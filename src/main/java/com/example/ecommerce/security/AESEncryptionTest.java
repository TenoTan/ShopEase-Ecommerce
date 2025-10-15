package com.example.ecommerce.security;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class AESEncryptionTest {

    @Bean
    @Profile("test-encryption")
    public CommandLineRunner testEncryption(AESEncryptionUtil encryptionUtil) {
        return args -> {
            System.out.println("===== TESTING AES ENCRYPTION =====");
            
            // Test data
            String sensitiveData = "4111-1111-1111-1111"; // Example credit card number
            
            // Encrypt the data
            String encrypted = encryptionUtil.encrypt(sensitiveData);
            
            // Print encrypted value
            System.out.println("Original: " + sensitiveData);
            System.out.println("Encrypted: " + encrypted);
            
            // Decrypt and verify
            String decrypted = encryptionUtil.decrypt(encrypted);
            
            System.out.println("Decrypted: " + decrypted);
            System.out.println("Matches original: " + sensitiveData.equals(decrypted));
            
            System.out.println("===== TEST COMPLETED =====");
        };
    }
}