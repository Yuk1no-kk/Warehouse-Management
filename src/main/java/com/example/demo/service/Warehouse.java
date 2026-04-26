package com.example.demo.service;

import com.example.demo.model.*; // 导入model包的所有物品类、异常类
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

// 下面是你原来的Warehouse代码，不用改其他内容
public class Warehouse {
    private final double maxWeight;
    // 改成ObservableList，支持UI自动刷新
    private final ObservableList<Item> itemList;
    private double currentWeight;

    public Warehouse(double maxWeight) {
        if (maxWeight <= 0) {
            throw new IllegalArgumentException("仓库最大容量必须大于0");
        }
        this.maxWeight = maxWeight;
        this.itemList = FXCollections.observableArrayList();
        this.currentWeight = 0.0;
    }

    // 核心方法：添加物品
    public void addItem(Item item) {
        // 1. 查重
        boolean exists = itemList.stream()
                .anyMatch(i -> i.getName().equals(item.getName()) && i.getType().equals(item.getType()));
        if (exists) {
            throw new ItemAlreadyExistsException("物品[" + item.getName() + "]已存在");
        }
        // 2. 容量校验
        if (currentWeight + item.getWeight() > maxWeight) {
            throw new WarehouseFullException(
                    "仓库已满！当前已用：" + String.format("%.2f", currentWeight) + "kg，剩余：" + String.format("%.2f", maxWeight - currentWeight) + "kg"
            );
        }
        // 3. 添加
        itemList.add(item);
        currentWeight += item.getWeight();
    }

    // 核心方法：移除物品
    public void removeItem(Item item) {
        if (!itemList.contains(item)) {
            throw new ItemNotFoundException("物品[" + item.getName() + "]不存在");
        }
        itemList.remove(item);
        currentWeight -= item.getWeight();
    }

    // 核心方法：获取物品列表（给UI绑定用）
    public ObservableList<Item> getItemList() {
        return itemList;
    }

    // 容量相关getter
    public double getMaxWeight() { return maxWeight; }
    public double getCurrentWeight() { return currentWeight; }
    // 计算容量使用率
    public double getCapacityUsage() { return currentWeight / maxWeight; }

    // 保留原有其他方法：查找物品、使用物品、筛选过期物品等
}