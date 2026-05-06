package com.example.demo.model;

import java.time.LocalDate;

public abstract class Consumable extends Item {
    private final LocalDate expiryDate;
    private boolean consumed;


    public Consumable(String name, double weight, String type, LocalDate expiryDate) {
        super(name, weight, type);
        this.expiryDate = expiryDate;
        this.consumed = false;
    }

    public abstract void consume();

    public boolean isExpired() {
        return LocalDate.now().isAfter(expiryDate);
    }

    // getter/setter
    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public boolean isConsumed() {
        return consumed;
    }

    public void setConsumed(boolean consumed) {
        this.consumed = consumed;
    }


    @Override
    public String getDurabilityOrExpiry() {
        return "expired date：" + expiryDate.toString();
    }

    @Override
    public String getSpecialAttribute() {
        return isConsumed() ? "already used" : "able to use";
    }
}