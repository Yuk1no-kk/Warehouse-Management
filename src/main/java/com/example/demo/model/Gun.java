package com.example.demo.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Gun extends Weapon {
    private final IntegerProperty bullets;


    public Gun(String name, double weight, int durability, int bullets) {
        super(name, weight, "Gun", durability);
        this.bullets = new SimpleIntegerProperty(bullets);
    }


    @Override
    public void use() {
        if (getDurability() <= 0) {
            throw new ItemBrokenException("gun[" + getName() + "]damaged, cannot be used");
        }
        if (bullets.get() <= 0) {
            throw new NoBulletsException("gun[" + getName() + "]run out of bullets, cannot be used");
        }

        bullets.set(bullets.get() - 1);
        setDurability(getDurability() - 10);
    }

    // getter/setter
    public IntegerProperty bulletsProperty() {
        return bullets;
    }

    public int getBullets() {
        return bullets.get();
    }


    @Override
    public String getSpecialAttribute() {
        return "bullet left：" + bullets.get() + "shots";
    }
}