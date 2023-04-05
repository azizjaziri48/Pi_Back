package com.example.pi_back.Services;

public class InvalidRIBException extends RuntimeException {
    public InvalidRIBException(String message) {
        super(message);
    }
}