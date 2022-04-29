package ru.restvoting.error;

public class IllegalDateTimeException extends RuntimeException {
    public IllegalDateTimeException(String message) {
        super(message);
    }
}