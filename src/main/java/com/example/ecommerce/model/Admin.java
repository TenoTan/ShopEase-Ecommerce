package com.example.ecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ADMINS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String name;
    private String phone;
    private String password;
    private String address;
    private String houseNumber;
    private String street;
    private String city;
    private String country;

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Seller> sellers = new ArrayList<>();

    // Helper method to add a seller
    public void addSeller(Seller seller) {
        sellers.add(seller);
        seller.setAdmin(this);
    }

    // Helper method to remove a seller
    public void removeSeller(Seller seller) {
        sellers.remove(seller);
        seller.setAdmin(null);
    }
}
