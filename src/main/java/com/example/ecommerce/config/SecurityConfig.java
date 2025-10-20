package com.example.ecommerce.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private CustomAuthenticationProvider authenticationProvider;

    @Autowired
    private CustomAuthenticationFailureHandler authenticationFailureHandler;

    @Bean
    @Order(1)
    public SecurityFilterChain sellerSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(AntPathRequestMatcher.antMatcher("/api/seller/**"))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/otp/**").permitAll()
                        .requestMatchers("/api/seller/**", "/selleranalytics.html", "/sellerhomepage.html").hasRole("SELLER")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/sellerlogin.html")
                        .loginProcessingUrl("/api/seller/login")
                        .failureHandler(authenticationFailureHandler)
                        .permitAll()
                )
                .authenticationProvider(authenticationProvider);
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(AntPathRequestMatcher.antMatcher("/api/admin/**"))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/otp/**").permitAll()
                        .requestMatchers("/api/admin/**", "/adminanalytics.html", "/adminselleranalytics.html", "/adminproductanalytics.html", "/adminseller.html","/adminsecuritydashboard").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/adminlogin.html")
                        .loginProcessingUrl("/api/admin/login")
                        .failureHandler(authenticationFailureHandler)
                        .permitAll()
                )
                .authenticationProvider(authenticationProvider);
        return http.build();
    }

    @Bean
    @Order(3)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // OTP and login pages - MUST be accessible without authentication
                        .requestMatchers("/customerotp.html", "/sellerotp.html", "/adminotp.html",
                                "/customerslogin.html", "/sellerlogin.html", "/adminlogin.html").permitAll()
                        .requestMatchers("/api/otp/**").permitAll()

                        // Public pages
                        .requestMatchers("/", "/ecom.html", "/css/**", "/js/**", "/images/**",
                                "/customer.html", "/seller.html",
                                "/api/public/**", "/Aboutus.html", "/beauty.html", "/phones.html",
                                "/books.html", "/shoes.html", "/furniture.html", "/toys.html",
                                "/appliances.html").permitAll()

                        // Customer-specific pages (protected)
                        .requestMatchers("/api/customer/**", "/postlogin.html", "/fashion.html",
                                "/product.html", "/productdetail.html", "/cart.html",
                                "/orderconfirmation.html", "/payment.html",
                                "/aboutuslogin.html").hasRole("CUSTOMER")

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginProcessingUrl("/api/customer/login")
                        .failureHandler(authenticationFailureHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/api/logout")
                        .logoutSuccessUrl("/ecom.html")
                        .permitAll()
                );

        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }
}