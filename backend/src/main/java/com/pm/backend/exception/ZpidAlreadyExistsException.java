package com.pm.backend.exception;

public class ZpidAlreadyExistsException extends RuntimeException {
    public ZpidAlreadyExistsException(String message) {
        super(message);
    }
}
