package com.example.ecommerce.controller;

import com.example.ecommerce.model.AuditLog;
import com.example.ecommerce.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.List;

@Controller
public class AdminDashboardController {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @GetMapping("/adminsecuritydashboard")
    public String getAdminSecurityDashboard(Model model) {
        List<AuditLog> allLogs = auditLogRepository.findAll(); // Shows everything
        List<AuditLog> failedLogins = auditLogRepository.findByAction("FAILED_LOGIN");
        List<AuditLog> successfulLogins = auditLogRepository.findByAction("LOGIN_AFTER_OTP");

        model.addAttribute("recentLogs", allLogs);
        model.addAttribute("failedLogins", failedLogins);
        model.addAttribute("successfulLogins", successfulLogins);
        model.addAttribute("totalLogs", allLogs.size());

        return "adminsecuritydashboard";
    }

}
