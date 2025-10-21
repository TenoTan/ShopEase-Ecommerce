package com.example.ecommerce.controller;

import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.Customer;
import com.example.ecommerce.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/api/public/customers")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/register")
    public String registerCustomer(Customer customer, String confirmPassword,
                                   RedirectAttributes redirectAttributes, Model model) {

        // Strong password validation
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        if (!customer.getPassword().matches(passwordPattern)) {
            model.addAttribute("error",
                    "Password must be at least 8 characters and contain: " +
                            "1 uppercase letter, 1 lowercase letter, 1 number, and 1 special character (@$!%*?&)");
            return "customer";
        }

        // Password match validation
        if (!customer.getPassword().equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "customer";
        }

        // Email already exists check
        if (customerService.getCustomerByEmail(customer.getEmail()).isPresent()) {
            model.addAttribute("error", "Email already registered");
            return "customer";
        }

        // Create cart and save customer
        Cart cart = new Cart();
        cart.setCustomer(customer);
        customer.setCart(cart);

        customerService.saveCustomer(customer);

        redirectAttributes.addFlashAttribute("registrationSuccess",
                "Registration successful! You can now login.");
        return "redirect:/ecom.html";
    }
}
