package com.example.ecommerce.config;

import com.example.ecommerce.model.AuditLog;
import com.example.ecommerce.model.Customer;
import com.example.ecommerce.model.Admin;
import com.example.ecommerce.model.Seller;
import com.example.ecommerce.service.AdminService;
import com.example.ecommerce.service.CustomerService;
import com.example.ecommerce.service.SellerService;
import com.example.ecommerce.repository.AuditLogRepository;
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

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/otp")
public class OtpVerificationController {

    @Autowired
    private OtpCacheService otpCacheService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SellerService sellerService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private AuditLogRepository auditLogRepository;

    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    @PostMapping("/verify")
    public ResponseEntity<?> verifyOtp(@RequestParam String email,
                                       @RequestParam String otp,
                                       @RequestParam String role,
                                       HttpServletRequest request,
                                       HttpServletResponse response) {
        // Validate OTP
        var otpDetails = otpCacheService.getOtpDetails(email);

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

        // Remove OTP from cache
        otpCacheService.removeOtp(email);

        // Create authentication token
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(email, null,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())));

        // Create security context with authentication and save it
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authToken);
        SecurityContextHolder.setContext(context);
        securityContextRepository.saveContext(context, request, response);

        // Save audit log for successful OTP login
        Long userId = fetchUserIdByEmail(email, role);

        AuditLog log = new AuditLog();
        log.setUserId(userId);
        log.setAction("LOGIN");
        log.setTimestamp(LocalDateTime.now());
        log.setDetails("User logged in after OTP verification: " + email);

        auditLogRepository.save(log);

        // Build success response
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

    private Long fetchUserIdByEmail(String email, String role) {
        switch (role.toUpperCase()) {
            case "CUSTOMER":
                return customerService.getCustomerByEmail(email)
                        .map(Customer::getId)
                        .orElse(null);
            case "SELLER":
                return sellerService.getSellerByEmail(email)
                        .map(Seller::getId)
                        .orElse(null);
            case "ADMIN":
                return adminService.getAdminByEmail(email)
                        .map(Admin::getId)
                        .orElse(null);
            default:
                return null;
        }
    }
}
