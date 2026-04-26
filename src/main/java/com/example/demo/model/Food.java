package com.example.demo.model;

import java.time.LocalDate;

public class Food extends Consumable {
    public Food(String name, double weight, LocalDate expiryDate) {

        super(name, weight, "Food", expiryDate);
    }

    @Override
    public void consume() {
        if (isExpired()) {
            throw new ExpiredItemException("food[" + getName() + "]expired, cannot be eaten");
        }
        if (isConsumed()) {
            throw new IllegalStateException("food[" + getName() + "]expired, cannot be eaten again");
        }
        setConsumed(true);
    }

    @Override
    public String getSpecialAttribute() {
        return isConsumed() ? "already eat" : "able to eat";
    }
}