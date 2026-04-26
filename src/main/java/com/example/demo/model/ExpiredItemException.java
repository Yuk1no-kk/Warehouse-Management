package com.example.demo.model;

// throw exception with have expired item
public class ExpiredItemException extends RuntimeException {
    public ExpiredItemException(String message) {
        super(message);
    }
}