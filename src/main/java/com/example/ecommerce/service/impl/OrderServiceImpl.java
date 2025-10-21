package com.example.ecommerce.service.impl;

import com.example.ecommerce.model.Order;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.ecommerce.repository.OrderItemRepository;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public boolean hasCustomerPurchasedProduct(Long customerId, Long productId) {
        Long result = orderItemRepository.existsByCustomerIdAndProductId(customerId, productId);
        return result != null && result == 1L;
    }

    @Override
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public List<Order> getOrdersByCustomerId(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    @Override
    public List<Order> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByDateRange(startDate, endDate);
    }

    @Override
    public BigDecimal calculateTotalSalesForPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.calculateTotalSalesForPeriod(startDate, endDate);
    }

    @Override
    public BigDecimal getTotalSalesForAllSellers() {
        return orderRepository.getTotalSalesForAllSellers();
    }

    @Override
    public List<Object[]> getMonthlySales(Integer year) {
        return orderRepository.getMonthlySales(year);
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public Map<Long, Long> getOrderCountByCustomer() {
        return orderRepository.countOrdersByCustomer()
                .stream()
                .collect(Collectors.toMap(
                        row -> ((Number) row[0]).longValue(),
                        row -> ((Number) row[1]).longValue()
                ));
    }

    // NEW METHOD - Add this
    @Override
    public List<Order> getOrdersWithDetails(Long customerId) {
        return orderRepository.findAllWithCustomer().stream()
                .filter(order -> order.getCustomer().getId().equals(customerId))
                .collect(Collectors.toList());
    }
}
