package com.example.ecommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OtpPageController {

    @GetMapping("/customerotp.html")
    public String customerOtp() {
        return "customerotp";
    }

    @GetMapping("/sellerotp.html")
    public String sellerOtp() {
        return "sellerotp";
    }

    @GetMapping("/adminotp.html")
    public String adminOtp() {
        return "adminotp";
    }
}
