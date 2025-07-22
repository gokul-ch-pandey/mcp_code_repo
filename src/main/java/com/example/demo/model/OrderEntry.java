package com.example.demo.model;

public class OrderEntry {
    private String productCode;
    private String productName;
    private int quantity;
    private double price;

    public OrderEntry() {}

    public OrderEntry(String productCode, String productName, int quantity, double price) {
        this.productCode = productCode;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }

    public String getProductCode() { return productCode; }
    public void setProductCode(String productCode) { this.productCode = productCode; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    @Override
    public String toString() {
        return "OrderEntry{" +
                "productCode='" + productCode + '\'' +
                ", productName='" + productName + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}
