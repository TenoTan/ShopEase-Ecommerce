package com.example.ecommerce.controller;

import com.example.ecommerce.repository.AuditLogRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

public class AuditLogController {

    private final AuditLogRepository auditLogRepository;

    public AuditLogController(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @GetMapping("/audit-logs")
    public String getAuditLogs(Model model) {
        var logs = auditLogRepository.findAll(Sort.by(Sort.Direction.DESC, "timestamp"));
        model.addAttribute("auditLogs", logs);
        return "audit-logs";
    }
}
