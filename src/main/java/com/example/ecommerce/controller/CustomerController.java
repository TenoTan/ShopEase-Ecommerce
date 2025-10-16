package com.example.ecommerce.controller;

import com.example.ecommerce.model.AuditLog;
import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.Customer;
import com.example.ecommerce.repository.AuditLogRepository;
import com.example.ecommerce.service.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/api/public/customers")
public class CustomerController {

    private final CustomerService customerService;
    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/register")
    public String registerCustomer(Customer customer, String confirmPassword,
                                   RedirectAttributes redirectAttributes, Model model, HttpServletRequest request) {
        // Validate input
        if (!customer.getPassword().equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "customer";
        }

        // Check if email already exists
        if (customerService.getCustomerByEmail(customer.getEmail()).isPresent()) {
            model.addAttribute("error", "Email already registered");
            return "customer";
        }

        // Create a cart for the customer
        Cart cart = new Cart();
        cart.setCustomer(customer);
        customer.setCart(cart);

        // Save the customer
        customerService.saveCustomer(customer);
        AuditLog log = new AuditLog();
        log.setUserId(customer.getId());
        log.setAction("CUSTOMER_SIGNUP");
        log.setTimestamp(LocalDateTime.now());
        log.setDetails("Customer signed up with email: " + customer.getEmail());
        log.setIpAddress(request.getRemoteAddr());
        auditLogRepository.save(log);


        // Add success message and redirect to homepage
        redirectAttributes.addFlashAttribute("registrationSuccess",
                "Registration successful! You can now login.");
        return "redirect:/ecom.html";
    }
}
