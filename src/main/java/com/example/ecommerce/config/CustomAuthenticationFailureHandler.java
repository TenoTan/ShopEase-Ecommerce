package com.example.ecommerce.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        System.out.println("=== FAILURE HANDLER CALLED ===");
        System.out.println("Exception type: " + exception.getClass().getName());
        System.out.println("Exception message: " + exception.getMessage());
        System.out.println("Response committed BEFORE: " + response.isCommitted());

        String email = request.getParameter("username");

        if (exception instanceof OtpRequiredException) {
            String requestURI = request.getRequestURI();
            String role = "";
            String otpPage = "";

            if (requestURI.contains("/api/admin/login")) {
                role = "ADMIN";
                otpPage = "/adminotp.html";
            } else if (requestURI.contains("/api/seller/login")) {
                role = "SELLER";
                otpPage = "/sellerotp.html";
            } else if (requestURI.contains("/api/customer/login")) {
                role = "CUSTOMER";
                otpPage = "/customerotp.html";
            }

            String encodedEmail = URLEncoder.encode(email, StandardCharsets.UTF_8);
            String redirectUrl = otpPage + "?email=" + encodedEmail + "&role=" + role;

            System.out.println("Redirecting to: " + redirectUrl);
            System.out.println("Response committed AFTER calculation: " + response.isCommitted());

            getRedirectStrategy().sendRedirect(request, response, redirectUrl);

            System.out.println("Response committed AFTER sendRedirect: " + response.isCommitted());
        } else {
            System.out.println("Regular authentication failure, using default handler");
            super.onAuthenticationFailure(request, response, exception);
        }
    }
}
