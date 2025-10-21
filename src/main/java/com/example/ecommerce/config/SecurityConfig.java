package com.example.ecommerce.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private CustomAuthenticationProvider authenticationProvider;

    @Autowired
    private CustomAuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private RateLimitFilter rateLimitFilter;

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
                .addFilterBefore(rateLimitFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(form -> form
                        .loginPage("/sellerlogin.html")
                        .loginProcessingUrl("/api/seller/login")
                        .failureUrl("/sellerlogin.html?error=true")
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
                        .requestMatchers("/api/admin/**", "/adminanalytics.html", "/adminselleranalytics.html", "/adminproductanalytics.html", "/adminseller.html").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(rateLimitFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(form -> form
                        .loginPage("/adminlogin.html")
                        .loginProcessingUrl("/api/admin/login")
                        .failureUrl("/adminlogin.html?error=true")
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
                        .requestMatchers("/customerotp.html", "/sellerotp.html", "/adminotp.html",
                                "/customerslogin.html", "/sellerlogin.html", "/adminlogin.html").permitAll()
                        .requestMatchers("/api/otp/**").permitAll()
                        .requestMatchers("/", "/ecom.html", "/css/**", "/js/**", "/images/**",
                                "/customer.html", "/seller.html",
                                "/api/public/**", "/Aboutus.html", "/beauty.html", "/phones.html",
                                "/books.html", "/shoes.html", "/furniture.html", "/toys.html",
                                "/appliances.html").permitAll()
                        .requestMatchers("/api/customer/**", "/postlogin.html", "/fashion.html",
                                "/product.html", "/productdetail.html", "/cart.html",
                                "/orderconfirmation.html", "/payment.html",
                                "/myorders.html", "/myorders", "/aboutuslogin.html").hasRole("CUSTOMER")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(rateLimitFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(form -> form
                        .loginPage("/customerslogin.html")
                        .loginProcessingUrl("/api/customer/login")
                        .failureUrl("/customerslogin.html?error=true")
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
