package com.example.ecommerce.service;

import com.example.ecommerce.model.Admin;
import java.util.List;
import java.util.Optional;

public interface AdminService {
    Admin saveAdmin(Admin admin);
    List<Admin> getAllAdmins();

        Optional<Admin> getAdminByEmail(String email);


    Optional<Admin> getAdminById(Long id);
    Optional<Admin> getAdminByName(String name);
    void deleteAdmin(Long id);
}
