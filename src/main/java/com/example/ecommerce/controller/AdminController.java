package com.example.ecommerce.controller;

import com.example.ecommerce.model.Admin;
import com.example.ecommerce.model.Seller;
import com.example.ecommerce.service.AdminService;
import com.example.ecommerce.service.OrderService;
import com.example.ecommerce.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class AdminController {
    private final AdminService adminService;
    private final SellerService sellerService;
    private final OrderService orderService;

    @Autowired
    public AdminController(AdminService adminService, SellerService sellerService, OrderService orderService) {
        this.orderService = orderService;
        this.adminService = adminService;
        this.sellerService = sellerService;
    }

    @GetMapping("/adminseller.html")
    public String adminSellers(Authentication authentication, Model model) {
        try {
            Long adminId = Long.parseLong(authentication.getName());
            Admin admin = adminService.getAdminById(adminId)
                    .orElseThrow(() -> new RuntimeException("Admin not found"));

            List<Seller> sellers = sellerService.getAllSellers();
            model.addAttribute("admin", admin);
            model.addAttribute("sellers", sellers);
            return "adminseller";
        } catch (Exception e) {
            System.err.println("Error in adminSellers: " + e.getMessage());
            return "redirect:/adminlogin.html?error=true";
        }
    }

    @PostMapping("/admin/seller/{id}/remove")
    public String removeSeller(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        sellerService.removeSeller(id);
        redirectAttributes.addFlashAttribute("success", "Seller removed successfully");
        return "redirect:/adminseller.html";
    }
    @GetMapping("/adminanalytics.html")
    public String adminAnalytics(Model model) {
        // Example: Add total sellers and total sales as attributes
        int totalSellers = sellerService.getAllSellers().size();
        BigDecimal totalSales = orderService.getTotalSalesForAllSellers();

        model.addAttribute("totalSellers", totalSellers);
        model.addAttribute("totalSales", totalSales);

        // Add more analytics as needed...

        return "adminanalytics"; // This is the name of your Thymeleaf template
    }



    @GetMapping("/admin/selleranalytics.html")
    public String sellerAnalytics(@RequestParam("sellerId") Long sellerId, Model model) {
        // Fetch analytics for this seller
        Seller seller = sellerService.getSellerById(sellerId)
                .orElseThrow(() -> new RuntimeException("Seller not found"));
        model.addAttribute("seller", seller);
        // Add seller analytics data to model
        return "adminselleranalytics"; // Thymeleaf template for seller analytics
    }
}
