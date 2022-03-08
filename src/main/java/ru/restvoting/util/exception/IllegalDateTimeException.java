package ru.restvoting.util.exception;

public class IllegalDateTimeException extends RuntimeException {
    public IllegalDateTimeException(String message) {
        super(message);
    }
}