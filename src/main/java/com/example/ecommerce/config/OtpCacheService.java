package com.example.ecommerce.config;

import com.example.ecommerce.model.OtpDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpCacheService {
    private final ConcurrentHashMap<String, OtpDetails> otpCache = new ConcurrentHashMap<>();

    public void storeOtp(String email, String otp) {
        otpCache.put(email, new OtpDetails(otp, LocalDateTime.now().plusMinutes(5)));
    }

    public OtpDetails getOtpDetails(String email) {
        return otpCache.get(email);
    }

    public void removeOtp(String email) {
        otpCache.remove(email);
    }
}
