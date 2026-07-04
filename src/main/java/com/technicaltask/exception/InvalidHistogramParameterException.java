package com.technicaltask.exception;

public class InvalidHistogramParameterException extends RuntimeException {

    public InvalidHistogramParameterException(String parameter) {
        super("Unsupported histogram parameter: %s".formatted(parameter));
    }
}
