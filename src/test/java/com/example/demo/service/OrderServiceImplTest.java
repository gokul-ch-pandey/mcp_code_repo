package com.example.demo.service;

import com.example.demo.model.Order;
import com.example.demo.model.OrderEntry;
import com.example.demo.model.OrderStatus;
import com.example.demo.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class OrderServiceImplTest {
    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        orderService = new OrderServiceImpl(java.time.Clock.systemDefaultZone());
    }

    @Test
    public void testCreateOrder() {
        OrderEntry entry = new OrderEntry("P1", "Product 1", 2, 50.0);
        Order order = new Order(null, "Test Order", null, java.time.LocalDate.now(), null, List.of(entry));
        Order created = orderService.createOrder(order);
        assertNotNull(created.getId());
        assertEquals("Test Order", created.getDescription());
        assertEquals(100.0, created.getAmount());
        assertEquals(OrderStatus.CREATED, created.getStatus());
        assertEquals(1, created.getEntries().size());
    }

    @Test
    public void testGetOrder() {
        OrderEntry entry = new OrderEntry("P1", "Product 1", 2, 50.0);
        Order order = new Order(null, "Test Order", null, java.time.LocalDate.now(), null, List.of(entry));
        Order created = orderService.createOrder(order);
        Order found = orderService.getOrder(created.getId());
        assertNotNull(found);
        assertEquals(created.getId(), found.getId());
    }

    @Test
    public void testGetAllOrders() {
        OrderEntry entry1 = new OrderEntry("P1", "Product 1", 1, 10.0);
        OrderEntry entry2 = new OrderEntry("P2", "Product 2", 2, 10.0);
        orderService.createOrder(new Order(null, "Order1", null, java.time.LocalDate.now(), null, List.of(entry1)));
        orderService.createOrder(new Order(null, "Order2", null, java.time.LocalDate.now(), null, List.of(entry2)));
        List<Order> orders = orderService.getAllOrders();
        assertEquals(2, orders.size());
    }

    @Test
    public void testUpdateOrder() {
        OrderEntry entry = new OrderEntry("P1", "Product 1", 1, 10.0);
        Order created = orderService.createOrder(new Order(null, "Old", null, java.time.LocalDate.now(), null, List.of(entry)));
        OrderEntry newEntry = new OrderEntry("P2", "Product 2", 2, 10.0);
        Order update = new Order(null, "New", null, java.time.LocalDate.now(), OrderStatus.CREATED, List.of(newEntry));
        Order updated = orderService.updateOrder(created.getId(), update);
        assertEquals("New", updated.getDescription());
        assertEquals(20.0, updated.getAmount());
    }

    @Test
    public void testDeleteOrder() {
        OrderEntry entry = new OrderEntry("P1", "Product 1", 1, 10.0);
        Order created = orderService.createOrder(new Order(null, "ToDelete", null, java.time.LocalDate.now(), null, List.of(entry)));
        orderService.deleteOrder(created.getId());
        assertNull(orderService.getOrder(created.getId()));
    }
}
