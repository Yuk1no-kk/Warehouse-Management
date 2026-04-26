package com.example.demo.model;

// throw if house if full
public class WarehouseFullException extends RuntimeException {
    public WarehouseFullException(String message) {
        super(message);
    }
}