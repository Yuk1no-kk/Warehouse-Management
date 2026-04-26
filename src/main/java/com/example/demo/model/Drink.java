package com.example.demo.model;

import java.time.LocalDate;

public class Drink extends Consumable {
    public Drink(String name, double weight, LocalDate expiryDate) {
        super(name, weight, "Drink", expiryDate);
    }

    @Override
    public void consume() {
        if (isExpired()) {
            throw new ExpiredItemException("drink[" + getName() + "]expired, cannot be consumed");
        }
        if (isConsumed()) {
            throw new IllegalStateException("drink[" + getName() + "]expired, cannot be consumed again");
        }
        setConsumed(true);
    }

    @Override
    public String getSpecialAttribute() {
        return isConsumed() ? "already drink" : "able to drink";
    }
}