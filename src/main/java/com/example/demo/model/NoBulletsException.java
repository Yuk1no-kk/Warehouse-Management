package com.example.demo.model;

public class NoBulletsException extends RuntimeException {
    public NoBulletsException(String message) {
        super(message);
    }
}