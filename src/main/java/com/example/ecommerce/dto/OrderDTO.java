package com.example.ecommerce.dto;

import com.example.ecommerce.model.OrderItem;
import com.example.ecommerce.model.Payment;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderDTO {
    private Long id;
    private LocalDateTime orderDate;
    private String status;
    private BigDecimal amount;
    private Payment payment;
    private List<OrderItem> items;

    // Constructors
    public OrderDTO() {}

    public OrderDTO(Long id, LocalDateTime orderDate, String status, BigDecimal amount, Payment payment, List<OrderItem> items) {
        this.id = id;
        this.orderDate = orderDate;
        this.status = status;
        this.amount = amount;
        this.payment = payment;
        this.items = items;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
}
