package com.example.ecommerce.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private RateLimitInterceptor rateLimitInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Apply rate limiting to login and OTP endpoints
        registry.addInterceptor(rateLimitInterceptor)
                .addPathPatterns(
                        "/api/customer/login",      // Customer login
                        "/api/seller/login",        // Seller login
                        "/api/admin/login",         // Admin login
                        "/api/otp/verify",          // OTP verification
                        "/api/public/customers/register"  // Registration (prevent spam)
                );
    }
}
