package com.annotator.core.application.variantparser;

public class InvalidVariantFormat extends RuntimeException {
    public InvalidVariantFormat(String message) {
        super(message);
    }

    public InvalidVariantFormat(String message, Throwable cause) {
        super(message, cause);
    }
}
