package com.example.demo.service;

import com.example.demo.model.*; // 导入model包的所有物品类、异常类
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class Warehouse {
    private final double maxWeight;

    private final ObservableList<Item> itemList;
    private double currentWeight;

    public Warehouse(double maxWeight) {
        if (maxWeight <= 0) {
            throw new IllegalArgumentException("maxWeight must be greater than 0");
        }
        this.maxWeight = maxWeight;
        this.itemList = FXCollections.observableArrayList();
        this.currentWeight = 0.0;
    }


    public void addItem(Item item) {

        boolean exists = itemList.stream()
                .anyMatch(i -> i.getName().equals(item.getName()) && i.getType().equals(item.getType()));
        if (exists) {
            throw new ItemAlreadyExistsException("item[" + item.getName() + "]already exists");
        }

        if (currentWeight + item.getWeight() > maxWeight) {
            throw new WarehouseFullException(
                    "already full,used：" + String.format("%.2f", currentWeight) + "kg，lift：" + String.format("%.2f", maxWeight - currentWeight) + "kg"
            );
        }

        itemList.add(item);
        currentWeight += item.getWeight();
    }


    public void removeItem(Item item) {
        if (!itemList.contains(item)) {
            throw new ItemNotFoundException("item[" + item.getName() + "]does not exist");
        }
        itemList.remove(item);
        currentWeight -= item.getWeight();
    }

    public ObservableList<Item> getItemList() {
        return itemList;
    }

    public double getMaxWeight() { return maxWeight; }
    public double getCurrentWeight() { return currentWeight; }

    public double getCapacityUsage() { return currentWeight / maxWeight; }

}