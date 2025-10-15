package com.example.ecommerce.repository;

import com.example.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT * FROM PRODUCTS WHERE category_id = :categoryId", nativeQuery = true)
    List<Product> findByCategoryId(@Param("categoryId") Long categoryId);

    @Query(value = "SELECT * FROM PRODUCTS WHERE price BETWEEN :minPrice AND :maxPrice", nativeQuery = true)
    List<Product> findByPriceRange(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);

    @Query(value = "SELECT * FROM PRODUCTS WHERE name LIKE CONCAT('%', :keyword, '%') OR description LIKE CONCAT('%', :keyword, '%')", nativeQuery = true)
    List<Product> searchByKeyword(@Param("keyword") String keyword);

    @Query(value = "SELECT * FROM PRODUCTS ORDER BY price ASC", nativeQuery = true)
    List<Product> findAllOrderByPriceAsc();

    @Query(value = "SELECT * FROM PRODUCTS ORDER BY price DESC", nativeQuery = true)
    List<Product> findAllOrderByPriceDesc();

    @Query(value = "SELECT p.* FROM PRODUCTS p " +
            "LEFT JOIN ORDER_ITEMS oi ON p.product_id = oi.product_id " +
            "GROUP BY p.product_id " +
            "ORDER BY COUNT(oi.product_id) DESC " +
            "LIMIT 10", nativeQuery = true)
    List<Product> findTopSellingProducts();

    @Query(value = "SELECT * FROM PRODUCTS WHERE seller_id = :sellerId", nativeQuery = true)
    List<Product> findBySellerId(@Param("sellerId") Long sellerId);

    @Query("SELECT p FROM Product p WHERE p.seller.id = :sellerId AND " +
            "(LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Product> findBySellerIdAndKeyword(@Param("sellerId") Long sellerId, @Param("keyword") String keyword);

}
