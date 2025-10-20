package com.example.ecommerce.repository;

import com.example.ecommerce.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    // in AdminRepository.java
    Optional<Admin> findByEmail(String email);


    @Query(value = "SELECT * FROM ADMINS WHERE id = :id", nativeQuery = true)
    Optional<Admin> findAdminById(@Param("id") Long id);

    @Query(value = "SELECT * FROM ADMINS WHERE name = :name", nativeQuery = true)
    Optional<Admin> findByName(@Param("name") String name);

    @Query(value = "SELECT * FROM ADMINS WHERE phone = :phone", nativeQuery = true)
    Optional<Admin> findByPhone(@Param("phone") String phone);
}
