package com.example.demo.service.impl;

import com.example.demo.model.Order;
import com.example.demo.model.OrderEntry;
import com.example.demo.model.OrderStatus;
import com.example.demo.service.OrderService;

import jakarta.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    // Use thread-safe collections for a singleton service
    private final Map<Long, Order> orderRepo = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    private final Clock clock;

    @Autowired // Inject Clock for testability and time zone consistency
    public OrderServiceImpl(Clock clock) {
        this.clock = clock;
    }

    @PostConstruct
    public void initTestData() {
        // ... initTestData remains the same, but should use clock ...
        logger.info("Initializing test data for orders...");
        List<OrderEntry> entries1 = List.of(
            new OrderEntry("P1", "Product 1", 2, 50.0),
            new OrderEntry("P2", "Product 2", 1, 75.0)
        );
        Order order1 = new Order(null, "Sample Order 1", null, LocalDate.now(clock), null, entries1);
        createOrder(order1);

        List<OrderEntry> entries2 = List.of(
            new OrderEntry("P3", "Product 3", 3, 20.0)
        );
        Order order2 = new Order(null, "Sample Order 2", null, LocalDate.now(clock), null, entries2);
        createOrder(order2);
    }

    @Override
    public Order createOrder(Order order) {
        if (order.getEntries() == null || order.getEntries().isEmpty()) {
            logger.error("Order must have at least one entry");
            throw new IllegalArgumentException("Order must have at least one entry");
        }
        
        // Create a new Order object to ensure immutability of the input
        Order newOrder = new Order();
        newOrder.setDescription(order.getDescription());
        newOrder.setEntries(order.getEntries());

        double total = newOrder.getEntries().stream()
                .mapToDouble(e -> e.getPrice() * e.getQuantity())
                .sum();
        newOrder.setAmount(total);

        if (order.getOrderDate() == null) {
            newOrder.setOrderDate(LocalDate.now(clock)); // Use injected clock
        } else {
            newOrder.setOrderDate(order.getOrderDate());
        }

        newOrder.setStatus(OrderStatus.CREATED);
        newOrder.setId(idCounter.getAndIncrement()); // Use atomic long

        orderRepo.put(newOrder.getId(), newOrder);
        logger.info("Order created: {}", newOrder);
        return newOrder;
    }
    
    // ... getOrder and getAllOrders are mostly fine, but getAllOrders should still return a copy ...
    @Override
    public List<Order> getAllOrders() {
        logger.info("Fetching all orders");
        // Creating a new ArrayList is correct to prevent modification of the underlying values collection
        return new ArrayList<>(orderRepo.values());
    }

    @Override
    public Order getOrder(Long id) {
        logger.info("Fetching order with id: {}", id);
        return orderRepo.get(id);
    }
    
    @Override
    public Order updateOrder(Long id, Order orderUpdateRequest) {
        logger.info("Updating order with id: {}", id);

        // Fetch the existing order. This is the entity we will modify and save.
        Order existingOrder = orderRepo.get(id);

        // 1. Correctly handle non-existent order (your original fix)
        if (existingOrder == null) {
            throw new IllegalArgumentException("Order with id " + id + " not found");
        }

        // 2. Prevent updates on terminal-state orders
        if (existingOrder.getStatus() == OrderStatus.COMPLETED || existingOrder.getStatus() == OrderStatus.CANCELLED) {
            logger.error("Cannot update order {} with status {}", id, existingOrder.getStatus());
            throw new IllegalStateException("Cannot update completed or cancelled order");
        }

        // 3. Apply updates from the request to the existing order entity
        // Update description if provided
        if (orderUpdateRequest.getDescription() != null) {
             existingOrder.setDescription(orderUpdateRequest.getDescription());
        }

        // Update entries and recalculate amount if provided
        if (orderUpdateRequest.getEntries() != null && !orderUpdateRequest.getEntries().isEmpty()) {
            existingOrder.setEntries(orderUpdateRequest.getEntries());
            double total = orderUpdateRequest.getEntries().stream()
                    .mapToDouble(e -> e.getPrice() * e.getQuantity())
                    .sum();
            existingOrder.setAmount(total);
        }

        // Handle status transitions
        if (orderUpdateRequest.getStatus() != null && !orderUpdateRequest.getStatus().equals(existingOrder.getStatus())) {
            validateStatusTransition(existingOrder.getStatus(), orderUpdateRequest.getStatus());
            existingOrder.setStatus(orderUpdateRequest.getStatus());
        }

        // Persist the updated *existing* order
        orderRepo.put(id, existingOrder);
        logger.info("Order updated: {}", existingOrder);
        return existingOrder;
    }

    private void validateStatusTransition(OrderStatus from, OrderStatus to) {
        // Example logic: Only allow specific transitions, e.g., from PROCESSING to COMPLETED/CANCELLED
        boolean isValidTransition = (from == OrderStatus.PROCESSING && (to == OrderStatus.COMPLETED || to == OrderStatus.CANCELLED))
                                     || (from == OrderStatus.CREATED && to == OrderStatus.PROCESSING);

        if (!isValidTransition) {
             logger.error("Invalid status transition from {} to {}", from, to);
             throw new IllegalStateException("Invalid status transition from " + from + " to " + to);
        }
    }
    
    @Override
    public void deleteOrder(Long id) {
        logger.info("Deleting order with id: {}", id);
        Order order = orderRepo.get(id);
        if (order == null) {
            // Log this event, as it might indicate a client-side issue.
            logger.warn("Attempted to delete non-existent order with id: {}", id);
            return;
        }

        if (order.getStatus() == OrderStatus.COMPLETED) {
            logger.error("Cannot delete completed order with id: {}", id);
            throw new IllegalStateException("Cannot delete completed order");
        }
        orderRepo.remove(id);
        logger.info("Order with id {} deleted successfully.", id);
    }
}