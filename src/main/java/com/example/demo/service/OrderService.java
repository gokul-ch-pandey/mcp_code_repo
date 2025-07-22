package com.example.demo.service;

import com.example.demo.model.Order;
import java.util.List;

public interface OrderService {
    Order createOrder(Order order);
    Order getOrder(Long id);
    List<Order> getAllOrders();
    Order updateOrder(Long id, Order order);
    void deleteOrder(Long id);
}
