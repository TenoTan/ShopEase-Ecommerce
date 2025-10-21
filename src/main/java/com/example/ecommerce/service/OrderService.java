package com.example.ecommerce.service;

import com.example.ecommerce.model.Order;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface OrderService {
    Order saveOrder(Order order);
    List<Order> getAllOrders();
    Optional<Order> getOrderById(Long id);
    List<Order> getOrdersByCustomerId(Long customerId);
    List<Order> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    BigDecimal calculateTotalSalesForPeriod(LocalDateTime startDate, LocalDateTime endDate);
    List<Object[]> getMonthlySales(Integer year);
    void deleteOrder(Long id);
    BigDecimal getTotalSalesForAllSellers();
    boolean hasCustomerPurchasedProduct(Long customerId, Long productId);
    Map<Long, Long> getOrderCountByCustomer();

    // NEW METHOD - Add this
    List<Order> getOrdersWithDetails(Long customerId);
}
