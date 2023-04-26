package com.example.pi_back.Services;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( HttpStatus.BAD_REQUEST)

public class BadWordException extends RuntimeException {
    public BadWordException(String message) {
        super(message);
    }
}