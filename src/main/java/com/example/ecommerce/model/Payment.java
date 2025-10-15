package com.example.ecommerce.model;

import com.example.ecommerce.security.AttributeEncryptor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @Column(name = "order_id")
    private Long orderId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "order_id")
    private Order order;

    @Convert(converter = AttributeEncryptor.class)
    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "amount")
    private BigDecimal amount;
    
    @Convert(converter = AttributeEncryptor.class)
    @Column(name = "card_number_last_four", length = 4)
    private String cardNumberLastFour;
    
    @Convert(converter = AttributeEncryptor.class)
    @Column(name = "payment_details")
    private String paymentDetails;

    // --- Add these getters and setters ---
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public String getCardNumberLastFour() {
        return cardNumberLastFour;
    }
    
    public void setCardNumberLastFour(String cardNumberLastFour) {
        this.cardNumberLastFour = cardNumberLastFour;
    }
    
    public String getPaymentDetails() {
        return paymentDetails;
    }
    
    public void setPaymentDetails(String paymentDetails) {
        this.paymentDetails = paymentDetails;
    }
}


