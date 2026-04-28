package org.example.service;

public class ValidateRes {
    private boolean valid;
    private String message;

    public ValidateRes(boolean valid, String message) {
        this.valid = valid;
        this.message = message;
    }

    public boolean isValid() {
        return valid;
    }

    public String getMessage() {
        return message;
    }
}
