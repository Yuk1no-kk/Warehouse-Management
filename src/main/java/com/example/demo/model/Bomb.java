package com.example.demo.model;

public class Bomb extends Weapon {
    private boolean used;

    public Bomb(String name, double weight, int durability) {
        super(name, weight, "Bomb", durability);
        this.used = false;
    }

    public Bomb(String name, double weight) {
        this(name, weight, 100);
    }

    @Override
    public void use() {
        if (isUsed()) {
            throw new IllegalStateException("bomb[" + getName() + "]already used, cannot be used again");
        }
        if (getDurability() <= 0) {
            throw new ItemBrokenException("bomb[" + getName() + "]broken, cannot be used");
        }
        System.out.println("Boom! [" + getName() + "]exploded!");
        markAsUsed();
        setDurability(0);
    }


    public boolean isUsed() {
        return used;
    }

    public void markAsUsed() {
        this.used = true;
    }
}