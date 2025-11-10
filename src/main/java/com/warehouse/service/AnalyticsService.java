package com.warehouse.service;

import com.warehouse.model.Product;
import java.util.*;
import java.util.stream.Collectors;

public class AnalyticsService {

    public static void runAnalytics(Warehouse warehouse) {
        System.out.println("ANALYTICS");
        System.out.println("Total orders: " + warehouse.getTotalOrders());
        System.out.printf("Total profit: $%.2f%n", warehouse.getTotalProfit());

        Map<Product, Integer> initial = warehouse.getInitialStock();
        Map<Product, Integer> current = warehouse.getStock();

        // Get sold items directly
        Map<Product, Integer> sold = warehouse.getSold();


        // Calculate total sold items
        int totalItemsSold = sold.values().stream().mapToInt(Integer::intValue).sum();
        System.out.println("Total items sold: " + totalItemsSold);

        // Sort products by units sold
        List<Map.Entry<Product, Integer>> sortedProducts = sold.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .collect(Collectors.toList());

        // TOP 3
        System.out.println("\nTop 3 Best-Selling Products:");
        sortedProducts.stream()
                .filter(e -> e.getValue() > 0)
                .limit(3)
                .forEach(e ->
                        System.out.println(e.getKey().getName() + " : " + e.getValue())
                );

        // ALL PRODUCTS
        System.out.println("\nAll Products Sales:");
        sortedProducts.forEach(e ->
                System.out.println(e.getKey().getName() + " : " + e.getValue())
        );
    }
}
