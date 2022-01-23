package ru.otus.exception;

public class AppComponentsContainerException extends RuntimeException {
    public AppComponentsContainerException(Exception e) {
        super(e);
    }

    public AppComponentsContainerException(String message) {
        super(message);
    }
}
