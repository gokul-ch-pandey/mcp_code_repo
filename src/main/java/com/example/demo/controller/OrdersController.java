package com.example.demo.controller;


import com.example.demo.model.Order;
import com.example.demo.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrdersController {
    private static final Logger logger = LoggerFactory.getLogger(OrdersController.class);

    @Autowired
    private OrderService orderService;

    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        logger.info("Received request to create order: {}", order);
        return orderService.createOrder(order);
    }

    @GetMapping("/{id}")
    public Order getOrder(@PathVariable Long id) {
        logger.info("Received request to get order with id: {}", id);
        return orderService.getOrder(id);
    }

    @GetMapping
    public List<Order> getAllOrders() {
        logger.info("Received request to get all orders");
        return orderService.getAllOrders();
    }

    @PutMapping("/{id}")
    public Order updateOrder(@PathVariable Long id, @RequestBody Order order) {
        logger.info("Received request to update order with id: {}", id);
        return orderService.updateOrder(id, order);
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        logger.info("Received request to delete order with id: {}", id);
        orderService.deleteOrder(id);
    }
}
