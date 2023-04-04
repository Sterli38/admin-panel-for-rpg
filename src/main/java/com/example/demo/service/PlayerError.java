package com.example.demo.service;

import org.springframework.http.HttpStatus;

public class PlayerError {
    private HttpStatus statusCode;
    private String message;

    public PlayerError(HttpStatus statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = HttpStatus.valueOf(statusCode);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
