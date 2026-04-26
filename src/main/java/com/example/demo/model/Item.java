package com.example.demo.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public abstract class Item {

    private final StringProperty name;
    private final DoubleProperty weight;
    private final StringProperty type;

    public Item(String name, double weight, String type) {
        this.name = new SimpleStringProperty(name);
        this.weight = new SimpleDoubleProperty(weight);
        this.type = new SimpleStringProperty(type);
    }


    public StringProperty nameProperty() {
        return name;
    }

    public DoubleProperty weightProperty() {
        return weight;
    }

    public StringProperty typeProperty() {
        return type;
    }



    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public double getWeight() {
        return weight.get();
    }

    public void setWeight(double weight) {
        this.weight.set(weight);
    }

    public String getType() {
        return type.get();
    }

    public void setType(String type) {
        this.type.set(type);
    }


    public abstract String getDurabilityOrExpiry();
    public abstract String getSpecialAttribute();
}