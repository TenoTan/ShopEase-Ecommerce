package com.example.ecommerce.config;

import com.example.ecommerce.security.AESEncryptionUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import java.util.Base64;

@Configuration
public class AESEncryptionConfig {

    private final Environment environment;

    public AESEncryptionConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public AESEncryptionUtil aesEncryptionUtil() {
        AESEncryptionUtil aesEncryptionUtil = new AESEncryptionUtil();
        
        // If a key is defined in application properties, use it
        String configuredKey = environment.getProperty("app.encryption.key");
        if (configuredKey != null && !configuredKey.isEmpty()) {
            try {
                byte[] keyBytes = Base64.getDecoder().decode(configuredKey);
                aesEncryptionUtil.setSecretKey(keyBytes);
            } catch (Exception e) {
                // If the configured key is invalid, a new one will be generated
                System.err.println("Warning: Invalid encryption key in configuration. Using a generated key instead.");
            }
        }
        
        return aesEncryptionUtil;
    }
}