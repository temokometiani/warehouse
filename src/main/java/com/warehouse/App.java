package com.warehouse;

import com.warehouse.model.*;
import com.warehouse.service.*;

import java.util.*;
import java.util.concurrent.*;

public class App {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Order> queue = new LinkedBlockingQueue<>();
        Warehouse warehouse = new Warehouse(queue);

        // sample products
        List<Product> catalog = List.of(
                new Product("Laptop", 1000, 10),
                new Product("Phone", 700, 15),
                new Product("Tablet", 400, 20),
                new Product("TV-screen", 800, 25),
                new Product("Charger", 70, 30),
                new Product("Monitor", 500, 22)
        );

        // Add them to warehouse
        catalog.forEach(p -> warehouse.addProduct(p, p.getQuantity()));

        //  Start supplier thread
        Thread supplierThread = new Thread(new Supplier(warehouse, catalog));
        supplierThread.start();

        // Customers placing random orders
        ExecutorService customers = Executors.newFixedThreadPool(3);
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            customers.submit(() -> {
                Product product = catalog.get(random.nextInt(catalog.size()));
                int quantity = 1 + random.nextInt(3); // order 1–3 units
                queue.add(new Order(product, quantity));
            });
        }

        // Wait for all customers to submit orders
        customers.shutdown();
        customers.awaitTermination(5, TimeUnit.SECONDS);

        // Add STOP orders for workers
        for (int i = 0; i < 2; i++) {
            queue.add(new Order(new Product("STOP", 0, 0), -1));
        }

        // Start warehouse workers after all orders are submitted
        ExecutorService workers = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 2; i++) {
            workers.submit(new OrderProcessor(warehouse));
        }

        // Wait for all workers
        workers.shutdown();
        workers.awaitTermination(5, TimeUnit.SECONDS);


        //Wait for supplier to finish
        supplierThread.join();

        AnalyticsService.runAnalytics(warehouse);
    }
}
