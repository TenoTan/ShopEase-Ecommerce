package com.example.ecommerce.model;

import com.example.ecommerce.security.SafeAttributeEncryptor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "CUSTOMERS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String name;

    @Convert(converter = SafeAttributeEncryptor.class)
    @Column(name = "house_number")
    private String houseNumber;

    @Convert(converter = SafeAttributeEncryptor.class)
    private String street;

    private String city;
    private String country;

    private String password;

    @Convert(converter = SafeAttributeEncryptor.class)
    private String phone;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private Cart cart;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Order> orders;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Rating> ratings;
}
