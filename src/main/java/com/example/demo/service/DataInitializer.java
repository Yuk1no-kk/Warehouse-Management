package com.example.demo.service;

import com.example.demo.model.*;
import java.time.LocalDate;


public class DataInitializer {

    public static void initializeWithSampleItems(Warehouse warehouse) {
        try {
            // ------------------------------
            // Add Sample Drink Items
            // ------------------------------
            Drink water = new Drink("Mineral Water", 0.5, LocalDate.of(2026, 12, 31));
            Drink cola = new Drink("Cola", 0.33, LocalDate.of(2026, 10, 15));
            warehouse.addItem(water);
            warehouse.addItem(cola);

            // ------------------------------
            // Add Sample Food Items
            // ------------------------------
            Food apple = new Food("Fresh Apple", 0.2, LocalDate.of(2026, 5, 20));
            Food bread = new Food("Whole Wheat Bread", 0.4, LocalDate.of(2026, 5, 10));
            warehouse.addItem(apple);
            warehouse.addItem(bread);

            // ------------------------------
            // Add Sample Weapon Items
            // ------------------------------
            Gun pistol = new Gun("Pistol", 1.2, 100, 30);
            Gun rifle = new Gun("Assault Rifle", 3.5, 85, 50);
            Bomb grenade = new Bomb("Hand Grenade", 0.6, 90);
            Bomb c4 = new Bomb("C4 Explosive", 2.0, 100);
            warehouse.addItem(pistol);
            warehouse.addItem(rifle);
            warehouse.addItem(grenade);
            warehouse.addItem(c4);

        } catch (Exception e) {
            // Silent fail for initialization (should not happen with valid sample data)
            System.err.println("Warning: Failed to initialize sample items: " + e.getMessage());
        }
    }
}