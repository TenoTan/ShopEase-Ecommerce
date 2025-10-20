package com.example.ecommerce.listener;

import com.example.ecommerce.model.AuditLog;
import com.example.ecommerce.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Component
public class LoginFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private HttpServletRequest request;

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        String username = event.getAuthentication().getName();

        // Get client IP
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        } else {
            ipAddress = ipAddress.split(",")[0].trim();
        }

        // Create and save audit log entry
        AuditLog log = new AuditLog();
        log.setUserId(null); // user isn't authenticated yet
        log.setAction("FAILED_LOGIN");
        log.setTimestamp(LocalDateTime.now());
        log.setDetails("Failed login attempt for username: " + username);
        log.setIpAddress(ipAddress);

        auditLogRepository.save(log);
    }
}
