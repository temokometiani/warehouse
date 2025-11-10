# Warehouse Management System

This is a simple **Warehouse Management System** implemented in Java.  
It demonstrates multi-threading, concurrency, and analytics for managing products, orders, and suppliers.

---

## Features

- **Product Catalog**: List of products with price and initial stock.
- **Customer Orders**: Multiple customers place orders concurrently using threads.
- **Warehouse Workers**: Orders are processed from a `BlockingQueue` by warehouse worker threads.
- **Supplier**: Supplier threads can restock products while orders are being processed.
- **Concurrency Safety**: Uses `ConcurrentHashMap` and synchronized methods to handle concurrent stock updates.
- **Analytics**:
    - Total orders processed
    - Total profit
    - Top 3 best-selling products
    - Units sold and remaining stock per product

---


---

## How to Run

bash
git clone https://github.com/temokometiani/warehouse.git
cd warehouse

## Example Output

ANALYTICS :
Total orders: 10
Total profit: $9010.00
Total items sold: 25

Top 3 Best-Selling Products:
Phone : 8
Laptop : 5
Charger : 4

Product Summary (Sold | Remaining in Warehouse):
Phone : Sold = 8, Remaining = 7
Laptop : Sold = 5, Remaining = 5
...

