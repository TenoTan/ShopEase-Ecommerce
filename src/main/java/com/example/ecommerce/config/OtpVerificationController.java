package com.example.ecommerce.config;

import com.example.ecommerce.model.OtpDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/otp")
public class OtpVerificationController {

    @Autowired
    private OtpCacheService otpCacheService;

    private final SecurityContextRepository securityContextRepository =
            new HttpSessionSecurityContextRepository();

    @PostMapping("/verify")
    public ResponseEntity<?> verifyOtp(@RequestParam String email,
                                       @RequestParam String otp,
                                       @RequestParam String role,
                                       HttpServletRequest request,
                                       HttpServletResponse response) {
        OtpDetails otpDetails = otpCacheService.getOtpDetails(email);

        if (otpDetails == null || otpDetails.isExpired()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "OTP expired or not found");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        if (!otpDetails.getOtp().equals(otp)) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Invalid OTP");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        // OTP is valid - remove from cache
        otpCacheService.removeOtp(email);

        // Create authentication token
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                email,
                null,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
        );

        // Create new security context
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authToken);
        SecurityContextHolder.setContext(context);

        // CRITICAL: Save the context to session (Spring Security 6.x requirement)
        securityContextRepository.saveContext(context, request, response);

        // Return success response
        Map<String, String> successResponse = new HashMap<>();
        successResponse.put("status", "success");
        successResponse.put("message", "OTP verified successfully");
        successResponse.put("redirectUrl", determineRedirectUrl(role));

        return ResponseEntity.ok(successResponse);
    }

    private String determineRedirectUrl(String role) {
        switch (role.toUpperCase()) {
            case "ADMIN":
                return "/adminseller.html";
            case "SELLER":
                return "/sellerhomepage.html";
            case "CUSTOMER":
                return "/postlogin.html";
            default:
                return "/ecom.html";
        }
    }
}
