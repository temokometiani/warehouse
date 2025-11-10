package com.warehouse.service;

import com.warehouse.model.Product;
import java.util.List;
import java.util.Random;

public class Supplier implements Runnable {
    private final Warehouse warehouse;
    private final List<Product> catalog;
    private final Random random = new Random();

    public Supplier(Warehouse warehouse, List<Product> catalog) {
        this.warehouse = warehouse;
        this.catalog = catalog;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 5; i++) { // supplier restocks 5 times
                Product product = catalog.get(random.nextInt(catalog.size()));
                int quantityToAdd = 1 + random.nextInt(5); // add 1–5 units

                warehouse.restockProduct(product, quantityToAdd);

                System.out.println("[SUPPLIER] Added " + quantityToAdd + " units of " + product.getName());
                Thread.sleep(300);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
