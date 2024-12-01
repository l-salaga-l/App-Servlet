package org.example.appservlet.exception;

public class BadRequestException extends RuntimeException {
    private static final String BAD_REQUEST = "Некорректный запрос";

    public BadRequestException() {
        super(BAD_REQUEST);
    }

    public BadRequestException(String message) {
        super(message);
    }
}
