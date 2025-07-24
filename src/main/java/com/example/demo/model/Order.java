package com.example.demo.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Order {
    private Long id;
    private String description;
    private Double amount;
    private LocalDate orderDate;
    private OrderStatus status;
    private List<OrderEntry> entries;

    public Order() {}

    public Order(Long id, String description, Double amount, LocalDate orderDate, OrderStatus status, List<OrderEntry> entries) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.orderDate = orderDate;
        this.status = status;
        this.entries = entries;
    }

    // Copy constructor
    public Order(Order other) {
        this.id = other.id;
        this.description = other.description;
        this.amount = other.amount;
        this.orderDate = other.orderDate;
        this.status = other.status;
        // Deep copy of entries
        if (other.entries != null) {
            this.entries = other.entries.stream()
                               .map(OrderEntry::new)
                               .collect(Collectors.toList());
        } else {
            this.entries = new ArrayList<>();
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public LocalDate getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDate orderDate) { this.orderDate = orderDate; }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public List<OrderEntry> getEntries() { return entries; }
    public void setEntries(List<OrderEntry> entries) { this.entries = entries; }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", orderDate=" + orderDate +
                ", status=" + status +
                ", entries=" + entries +
                '}';
    }
}
