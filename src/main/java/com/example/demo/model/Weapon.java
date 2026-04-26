package com.example.demo.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public abstract class Weapon extends Item {

    private final IntegerProperty durability;


    public Weapon(String name, double weight, String type, int durability) {
        super(name, weight, type);
        this.durability = new SimpleIntegerProperty(durability);
    }


    public IntegerProperty durabilityProperty() {
        return durability;
    }


    public int getDurability() {
        return durability.get();
    }

    public void setDurability(int durability) {
        this.durability.set(durability);
    }


    public abstract void use();


    @Override
    public String getDurabilityOrExpiry() {
        return "durability：" + getDurability() + "/100";
    }

    @Override
    public String getSpecialAttribute() {
        return "Weapon";
    }
}