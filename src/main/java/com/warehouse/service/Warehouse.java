package com.warehouse.service;

import com.warehouse.model.Product;
import com.warehouse.model.Order;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.*;

public class Warehouse {
    private final ConcurrentHashMap<Product, Integer> stock = new ConcurrentHashMap<>();
    private final Map<Product, Integer> initialStock = new ConcurrentHashMap<>();
    private final BlockingQueue<Order> orderQueue;

    private final ConcurrentHashMap<Product, AtomicInteger> soldPerProduct = new ConcurrentHashMap<>();
    private final AtomicInteger totalOrders = new AtomicInteger(0);
    private final DoubleAdder totalProfit = new DoubleAdder();

    public Warehouse(BlockingQueue<Order> orderQueue) {
        this.orderQueue = orderQueue;
    }

    public void restockProduct(Product product, int quantity) {
        synchronized (product) {
            stock.compute(product, (p, q) -> (q == null ? quantity : q + quantity));
            product.restock(quantity);
        }
    }


    public void addProduct(Product product, int quantity) {
        stock.put(product, quantity);
        initialStock.put(product, quantity);
    }

    public boolean processOrder(Order order) {
        Product product = order.getProduct();
        int qty = order.getQuantity();

        return stock.compute(product, (p, available) -> {
            if (available == null || available < qty) {
                return available; // not enough stock
            }
            totalOrders.incrementAndGet();
            totalProfit.add(product.getPrice() * qty);

            // Track units sold
            soldPerProduct.computeIfAbsent(product, k -> new AtomicInteger()).addAndGet(qty);

            return available - qty;
        }) != null;
    }


    public ConcurrentHashMap<Product, Integer> getStock() {
        return stock;
    }

    public Map<Product, Integer> getInitialStock() {
        return initialStock;
    }

    public Map<Product, Integer> getSold() {
        Map<Product, Integer> result = new HashMap<>();
        soldPerProduct.forEach((p, q) -> result.put(p, q.get()));
        return result;
    }

    public BlockingQueue<Order> getOrderQueue() {
        return orderQueue;
    }

    public int getTotalOrders() {
        return totalOrders.get();
    }

    public double getTotalProfit() {
        return totalProfit.sum();
    }
}
