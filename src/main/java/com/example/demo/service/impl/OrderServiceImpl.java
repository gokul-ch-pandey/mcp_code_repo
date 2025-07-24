
 package com.example.demo.service.impl;

import com.example.demo.model.Order;
import com.example.demo.model.OrderEntry;
import com.example.demo.model.OrderStatus;
import com.example.demo.service.OrderService;

import jakarta.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    private final Map<Long, Order> orderRepo = new HashMap<>();
    private long idCounter = 1;

    @PostConstruct
    public void initTestData() {
        logger.info("Initializing test data for orders...");
        List<OrderEntry> entries1 = List.of(
            new OrderEntry("P1", "Product 1", 2, 50.0),
            new OrderEntry("P2", "Product 2", 1, 75.0)
        );
        Order order1 = new Order(null, "Sample Order 1", null, java.time.LocalDate.now(), null, entries1);
        createOrder(order1);

        List<OrderEntry> entries2 = List.of(
            new OrderEntry("P3", "Product 3", 3, 20.0)
        );
        Order order2 = new Order(null, "Sample Order 2", null, java.time.LocalDate.now(), null, entries2);
        createOrder(order2);
    }

    @Override
    public Order createOrder(Order order) {
        // Validate entries
        if (order.getEntries() == null || order.getEntries().isEmpty()) {
            logger.error("Order must have at least one entry");
            throw new IllegalArgumentException("Order must have at least one entry");
        }
        // Calculate total amount
        double total = order.getEntries().stream()
            .mapToDouble(e -> e.getPrice() * e.getQuantity())
            .sum();
        order.setAmount(total);
        // Set order date if not set
        if (order.getOrderDate() == null) {
            order.setOrderDate(java.time.LocalDate.now());
        }
        // Set status
        order.setStatus(OrderStatus.CREATED);
        order.setId(idCounter++);
        orderRepo.put(order.getId(), order);
        logger.info("Order created: {}", order);
        return order;
    }

    @Override
    public Order getOrder(Long id) {
        logger.info("Fetching order with id: {}", id);
        return orderRepo.get(id);
    }

    @Override
    public List<Order> getAllOrders() {
        logger.info("Fetching all orders");
        return new ArrayList<>(orderRepo.values());
    }

    @Override
    public Order updateOrder(Long id, Order order) {
        logger.info("Updating order with id: {}", id);
        Order existing = orderRepo.get(id);
        if (existing == null) {
            logger.error("Order with id {} not found", id);
            return null;
        }
        // Only allow update if not COMPLETED or CANCELLED
        if (existing.getStatus() == OrderStatus.COMPLETED || existing.getStatus() == OrderStatus.CANCELLED) {
            logger.error("Cannot update order with status {}", existing.getStatus());
            throw new IllegalStateException("Cannot update completed or cancelled order");
        }
        // Business logic: recalculate total, update status
        if (order.getEntries() != null && !order.getEntries().isEmpty()) {
            double total = order.getEntries().stream()
                .mapToDouble(e -> e.getPrice() * e.getQuantity())
                .sum();
            order.setAmount(total);
        } else {
            order.setAmount(existing.getAmount());
            order.setEntries(existing.getEntries());
        }
        order.setId(id);
        order.setOrderDate(existing.getOrderDate());
        // Status transition: allow only PROCESSING -> COMPLETED or CANCELLED
        if (order.getStatus() == null) {
            order.setStatus(existing.getStatus());
        } else if (existing.getStatus() == OrderStatus.PROCESSING &&
            (order.getStatus() == OrderStatus.COMPLETED || order.getStatus() == OrderStatus.CANCELLED)) {
            // allow
        } else if (!order.getStatus().equals(existing.getStatus())) {
            logger.error("Invalid status transition from {} to {}", existing.getStatus(), order.getStatus());
            throw new IllegalStateException("Invalid status transition");
        }
        orderRepo.put(id, order);
        logger.info("Order updated: {}", order);
        return order;
    }

    @Override
    public void deleteOrder(Long id) {
        logger.info("Deleting order with id: {}", id);
        Order order = orderRepo.get(id);
        if (order == null) return;
        // Only allow delete if not COMPLETED
        if (order.getStatus() == OrderStatus.COMPLETED) {
            logger.error("Cannot delete completed order with id: {}", id);
            throw new IllegalStateException("Cannot delete completed order");
        }
        orderRepo.remove(id);
    }
}
 