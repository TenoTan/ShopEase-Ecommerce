package com.example.ecommerce.config;

import com.example.ecommerce.model.Admin;
import com.example.ecommerce.model.Customer;
import com.example.ecommerce.model.Seller;
import com.example.ecommerce.service.CustomerService;
import com.example.ecommerce.service.SellerService;
import com.example.ecommerce.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;
import java.util.Optional;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final CustomerService customerService;
    private final SellerService sellerService;
    private final AdminService adminService;
    private final PasswordEncoder passwordEncoder;
    private final EmailOtpService emailOtpService;
    private final OtpCacheService otpCacheService;

    @Autowired
    public CustomAuthenticationProvider(CustomerService customerService,
                                        SellerService sellerService,
                                        AdminService adminService,
                                        PasswordEncoder passwordEncoder,
                                        EmailOtpService emailOtpService,
                                        OtpCacheService otpCacheService) {
        this.customerService = customerService;
        this.sellerService = sellerService;
        this.adminService = adminService;
        this.passwordEncoder = passwordEncoder;
        this.emailOtpService = emailOtpService;
        this.otpCacheService = otpCacheService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        String requestURI = "";
        try {
            requestURI = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                    .getRequest().getRequestURI();
            System.out.println("Request URI: " + requestURI);
        } catch (Exception e) {
            System.err.println("Error getting request URI: " + e.getMessage());
        }

        if (requestURI.contains("/api/admin/login")) {
            System.out.println("Attempting admin authentication with ID: " + email);
            return authenticateAdmin(email, password);
        } else if (requestURI.contains("/api/seller/login")) {
            return authenticateSeller(email, password);
        } else if (requestURI.contains("/api/customer/login")) {
            return authenticateCustomer(email, password);
        } else {
            try {
                return authenticateCustomer(email, password);
            } catch (BadCredentialsException e) {
                try {
                    return authenticateSeller(email, password);
                } catch (BadCredentialsException ex) {
                    throw new BadCredentialsException("Invalid email or password");
                }
            }
        }
    }

    private Authentication authenticateCustomer(String email, String password) {
        Optional<Customer> customerOpt = customerService.getCustomerByEmail(email);

        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            if (passwordEncoder.matches(password, customer.getPassword())) {
                // Password is correct - generate and send OTP
                String otp = emailOtpService.generateOTP();
                emailOtpService.sendOtpEmail(email, otp);
                otpCacheService.storeOtp(email, otp);

                System.out.println("OTP sent to customer: " + email);
                throw new OtpRequiredException("OTP sent to email. Please verify.");
            }
        }
        throw new BadCredentialsException("Invalid email or password");
    }

    private Authentication authenticateSeller(String email, String password) {
        Optional<Seller> sellerOpt = sellerService.getSellerByEmail(email);

        if (sellerOpt.isPresent()) {
            Seller seller = sellerOpt.get();
            if (passwordEncoder.matches(password, seller.getPassword())) {
                // Password is correct - generate and send OTP
                String otp = emailOtpService.generateOTP();
                emailOtpService.sendOtpEmail(email, otp);
                otpCacheService.storeOtp(email, otp);

                System.out.println("OTP sent to seller: " + email);
                throw new OtpRequiredException("OTP sent to email. Please verify.");
            }
        }
        throw new BadCredentialsException("Invalid email or password");
    }

    private Authentication authenticateAdmin(String adminIdStr, String password) {
        try {
            Long adminId = Long.parseLong(adminIdStr);
            Optional<Admin> adminOpt = adminService.getAdminById(adminId);

            if (adminOpt.isPresent()) {
                Admin admin = adminOpt.get();
                if (passwordEncoder.matches(password, admin.getPassword())) {
                    // For admin, you need an email field - using phone for now as placeholder
                    // TODO: Add email field to Admin model or use alternative notification method
                    String adminIdentifier = adminIdStr; // Store with admin ID as key
                    String otp = emailOtpService.generateOTP();

                    // You'll need to add email field to Admin or use another method
                    // For now, storing OTP with adminId as key
                    otpCacheService.storeOtp(adminIdentifier, otp);

                    // TODO: Send OTP to admin's email once email field is added
                    System.out.println("OTP generated for admin ID: " + adminId + " - OTP: " + otp);

                    throw new OtpRequiredException("OTP generated. Please verify.");
                }
            }
        } catch (NumberFormatException e) {
            throw new BadCredentialsException("Invalid Admin ID format");
        }
        throw new BadCredentialsException("Invalid Admin ID or password");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
