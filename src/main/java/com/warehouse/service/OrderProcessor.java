package com.warehouse.service;

import com.warehouse.model.Order;


public class OrderProcessor implements Runnable {
    private final Warehouse warehouse;

    public OrderProcessor(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Order order = warehouse.getOrderQueue().take();
                if (order.getQuantity() == -1) {
                    break;
                }

                boolean success = warehouse.processOrder(order);
                if (!success) {
                    System.out.println("Failed to process order: " + order.getProduct().getName());
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
