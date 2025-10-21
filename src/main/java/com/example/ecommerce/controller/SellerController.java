package com.example.ecommerce.controller;

import com.example.ecommerce.model.AuditLog;
import com.example.ecommerce.model.Seller;
import com.example.ecommerce.repository.AuditLogRepository;
import com.example.ecommerce.service.SellerService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
@RequestMapping("/api/public/sellers")
public class SellerController {
    private final SellerService sellerService;
    @Autowired
    private AuditLogRepository auditLogRepository;


    @Autowired
    public SellerController(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @PostMapping("/register")
    public String registerSeller(Seller seller,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 RedirectAttributes redirectAttributes,
                                 Model model, HttpServletRequest request) {
        // Validate input
        if (!seller.getPassword().equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "seller"; // Return to the registration form with error
        }

        // Check if email already exists
        if (sellerService.getSellerByEmail(seller.getEmail()).isPresent()) {
            model.addAttribute("error", "Email already registered");
            return "seller";
        }

        // Check if business name already exists
        if (sellerService.getSellerByBusinessName(seller.getBusinessName()).isPresent()) {
            model.addAttribute("error", "Business name already registered");
            return "seller";
        }

        try {
            // Save the seller
            sellerService.saveSeller(seller);
            AuditLog log = new AuditLog();
            log.setUserId(seller.getId());
            log.setAction("SELLER_SIGNUP");
            log.setTimestamp(java.time.LocalDateTime.now());
            log.setDetails("Seller signed up with email: " + seller.getEmail());
            log.setIpAddress(request.getRemoteAddr());
            auditLogRepository.save(log);


            // Add success message and redirect to homepage
            redirectAttributes.addFlashAttribute("registrationSuccess",
                    "Seller registration successful! You can now login.");
            return "redirect:/ecom.html";
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "seller";
        }
    }
}
