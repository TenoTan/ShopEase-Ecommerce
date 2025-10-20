package com.example.ecommerce.listener;

import com.example.ecommerce.model.AuditLog;
import com.example.ecommerce.model.Customer;
import com.example.ecommerce.repository.AuditLogRepository;
import com.example.ecommerce.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class LoginAuditListener {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private CustomerService customerService;

    @EventListener
    public void handleAuthenticationSuccess(AuthenticationSuccessEvent event) {
        System.out.println("Audit listener triggered for: " );

        String email = event.getAuthentication().getName();
        Customer customer = customerService.getCustomerByEmail(email).orElse(null);

        AuditLog log = new AuditLog();
        log.setUserId(customer != null ? customer.getId() : null);
        log.setAction("CUSTOMER_LOGIN");
        log.setTimestamp(LocalDateTime.now());
        log.setDetails("Customer logged in: " + email);
        log.setIpAddress(null); // IP logging is advanced

        try {
            auditLogRepository.save(log);
            System.out.println("Audit log saved.");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
