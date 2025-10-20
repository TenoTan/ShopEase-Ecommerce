package com.example.ecommerce.controller;
import org.springframework.ui.Model;

import com.example.ecommerce.model.AuditLog;
import com.example.ecommerce.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AdminSecurityController {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @GetMapping("/admin/security-dashboard")
    public String viewSecurityDashboard(Model model) {
        // Fetch all audit logs or specific security events
        List<AuditLog> allLogs = auditLogRepository.findAll();

        // Filter log types, for example:
        List<AuditLog> failedLogins = allLogs.stream()
                .filter(log -> "FAILED_LOGIN".equals(log.getAction()))
                .toList();

        List<AuditLog> successfulLogins = allLogs.stream()
                .filter(log -> "ADMIN_LOGIN".equals(log.getAction()))
                .toList();

        model.addAttribute("failedLogins", failedLogins);
        model.addAttribute("successfulLogins", successfulLogins);
        model.addAttribute("totalLogs", allLogs.size());
        model.addAttribute("recentLogs", allLogs.stream().limit(10).toList());

        return "adminsecuritydashboard"; // Thymeleaf template name
    }
}
