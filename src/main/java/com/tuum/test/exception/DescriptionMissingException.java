package com.tuum.test.exception;

public class DescriptionMissingException extends RuntimeException {

    public DescriptionMissingException(String message) {
        super(message);
    }

    public DescriptionMissingException(String message, Throwable cause) {
        super(message, cause);
    }
}
