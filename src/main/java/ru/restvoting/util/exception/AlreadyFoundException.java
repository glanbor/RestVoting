package ru.restvoting.util.exception;

public class AlreadyFoundException extends RuntimeException {
    public AlreadyFoundException(String message) {
        super(message);
    }
}