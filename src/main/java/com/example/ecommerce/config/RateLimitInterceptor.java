package com.example.ecommerce.config;

import com.example.ecommerce.service.RateLimitingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(RateLimitInterceptor.class);

    @Autowired
    private RateLimitingService rateLimitingService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String key = getClientIP(request);
        String uri = request.getRequestURI();

        logger.debug("Rate limit check for IP: {} on URI: {}", key, uri);

        if (!rateLimitingService.isAllowed(key)) {
            logger.warn("🚫 RATE LIMIT EXCEEDED for IP: {} on URI: {}", key, uri);
            response.setStatus(429); // Too Many Requests
            response.setContentType("text/html");
            response.getWriter().write(
                    "<html><body style='font-family: Arial; text-align: center; padding: 50px;'>" +
                            "<h1 style='color: #d32f2f;'>⚠️ Too Many Requests</h1>" +
                            "<p style='font-size: 18px;'>You have exceeded the maximum number of login attempts.</p>" +
                            "<p style='font-size: 16px;'>Please try again in <strong>1 minute</strong>.</p>" +
                            "<a href='/customerslogin.html' style='display: inline-block; margin-top: 20px; padding: 10px 20px; background: #1976d2; color: white; text-decoration: none; border-radius: 5px;'>Go Back</a>" +
                            "</body></html>"
            );
            return false;
        }

        logger.debug("✅ Rate limit OK for IP: {}", key);
        return true;
    }

    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
