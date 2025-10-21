package com.example.ecommerce.config;

import com.example.ecommerce.service.RateLimitingService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RateLimitFilter.class);

    @Autowired
    private RateLimitingService rateLimitingService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String uri = request.getRequestURI();

        // Only apply rate limiting to login endpoints
        if (uri.equals("/api/customer/login") ||
                uri.equals("/api/seller/login") ||
                uri.equals("/api/admin/login")) {

            String ip = getClientIP(request);
            logger.info("🔍 Checking rate limit for IP: {} on URI: {}", ip, uri);

            if (!rateLimitingService.isAllowed(ip)) {
                logger.warn("🚫 RATE LIMIT EXCEEDED for IP: {} on URI: {}", ip, uri);

                response.setStatus(429);
                response.setContentType("text/html; charset=UTF-8");
                response.getWriter().write(
                        "<!DOCTYPE html>" +
                                "<html>" +
                                "<head>" +
                                "    <meta charset='UTF-8'>" +
                                "    <style>" +
                                "        body { font-family: Arial, sans-serif; text-align: center; padding: 50px; background: #f5f5f5; }" +
                                "        .container { background: white; padding: 40px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); max-width: 500px; margin: 0 auto; }" +
                                "        h1 { color: #d32f2f; }" +
                                "        .btn { display: inline-block; margin-top: 20px; padding: 12px 24px; background: #1976d2; color: white; text-decoration: none; border-radius: 5px; }" +
                                "    </style>" +
                                "</head>" +
                                "<body>" +
                                "    <div class='container'>" +
                                "        <h1>⚠️ Too Many Requests</h1>" +
                                "        <p style='font-size: 18px;'>You have exceeded the maximum number of login attempts.</p>" +
                                "        <p style='font-size: 16px; color: #666;'>Please wait <strong>1 minute</strong> before trying again.</p>" +
                                "        <a href='/customerslogin.html' class='btn'>← Back to Login</a>" +
                                "    </div>" +
                                "</body>" +
                                "</html>"
                );
                return; // Stop processing
            }

            logger.info("✅ Rate limit OK for IP: {}", ip);
        }

        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }

    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
