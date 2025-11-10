package com.warehouse.model;

public class Product {
    private final String name;
    private final double price;
    private int quantity;

    public Product(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public synchronized int getQuantity() {
        return quantity;
    }

    public synchronized boolean reduceStock(int amount) {
        if (quantity >= amount) {
            quantity -= amount;
            return true;
        }
        return false;
    }

    public synchronized void restock(int amount) {
        quantity += amount;
    }

    @Override
    public String toString() {
        return name + " ($" + price + ", quantity: " + quantity + ")";
    }
}
