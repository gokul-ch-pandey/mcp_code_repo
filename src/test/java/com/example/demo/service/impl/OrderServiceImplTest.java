package com.example.demo.service.impl;

import com.example.demo.model.Order;
import com.example.demo.model.OrderEntry;
import com.example.demo.model.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    private OrderServiceImpl orderService;
    private Clock clock;

    @BeforeEach
    void setUp() {
        clock = Clock.fixed(Instant.parse("2024-01-01T10:00:00Z"), ZoneId.of("UTC"));
        orderService = new OrderServiceImpl(clock);
    }

    @Test
    void createOrder_shouldCreateOrderSuccessfully() {
        Order order = new Order();
        order.setDescription("Test Order");
        order.setEntries(List.of(new OrderEntry("P1", "Product 1", 1, 100.0)));

        Order createdOrder = orderService.createOrder(order);

        assertNotNull(createdOrder);
        assertEquals(1L, createdOrder.getId());
        assertEquals("Test Order", createdOrder.getDescription());
        assertEquals(100.0, createdOrder.getAmount());
        assertEquals(OrderStatus.CREATED, createdOrder.getStatus());
        assertEquals(LocalDate.now(clock), createdOrder.getOrderDate());
        assertEquals(1, orderService.getAllOrders().size());
    }

    @Test
    void createOrder_whenEntriesAreNull_shouldThrowException() {
        Order order = new Order();
        order.setDescription("Test Order");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(order);
        });

        assertEquals("Order must have at least one entry", exception.getMessage());
    }

    @Test
    void updateOrder_whenOrderExists_shouldUpdateSuccessfully() {
        Order order = new Order();
        order.setDescription("Initial Order");
        order.setEntries(List.of(new OrderEntry("P1", "Product 1", 1, 100.0)));
        Order createdOrder = orderService.createOrder(order);
        createdOrder.setStatus(OrderStatus.PROCESSING);


        Order updateRequest = new Order();
        updateRequest.setDescription("Updated Order");
        updateRequest.setStatus(OrderStatus.COMPLETED);


        Order updatedOrder = orderService.updateOrder(createdOrder.getId(), updateRequest);

        assertNotNull(updatedOrder);
        assertEquals("Updated Order", updatedOrder.getDescription());
        assertEquals(OrderStatus.COMPLETED, updatedOrder.getStatus());
    }

    @Test
    void updateOrder_whenOrderNotFound_shouldThrowException() {
        Order updateRequest = new Order();
        updateRequest.setDescription("Non-existent order");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.updateOrder(999L, updateRequest);
        });

        assertEquals("Order with id 999 not found", exception.getMessage());
    }

    @Test
    void updateOrder_whenStatusIsCompleted_shouldThrowException() {
        Order order = new Order();
        order.setDescription("Initial Order");
        order.setEntries(List.of(new OrderEntry("P1", "Product 1", 1, 100.0)));
        Order createdOrder = orderService.createOrder(order);
        createdOrder.setStatus(OrderStatus.COMPLETED);


        Order updateRequest = new Order();
        updateRequest.setDescription("Updated Order");


        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            orderService.updateOrder(createdOrder.getId(), updateRequest);
        });

        assertEquals("Cannot update completed or cancelled order", exception.getMessage());
    }

    @Test
    void updateOrder_withInvalidStatusTransition_shouldThrowException() {
        Order order = new Order();
        order.setDescription("Initial Order");
        order.setEntries(List.of(new OrderEntry("P1", "Product 1", 1, 100.0)));
        Order createdOrder = orderService.createOrder(order);

        Order updateRequest = new Order();
        updateRequest.setStatus(OrderStatus.CANCELLED); // Invalid transition from CREATED

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            orderService.updateOrder(createdOrder.getId(), updateRequest);
        });

        assertEquals("Invalid status transition from CREATED to CANCELLED", exception.getMessage());
    }
}