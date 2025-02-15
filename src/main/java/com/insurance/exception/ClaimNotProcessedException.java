package com.insurance.exception;

public class ClaimNotProcessedException extends RuntimeException {
    public ClaimNotProcessedException(String message) {
        super(message);
    }
}