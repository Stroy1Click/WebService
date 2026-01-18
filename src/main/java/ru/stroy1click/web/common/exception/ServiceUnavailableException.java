package ru.stroy1click.web.common.exception;

public class ServiceUnavailableException extends RuntimeException {

    public ServiceUnavailableException() {

    }

    public ServiceUnavailableException(String message) {
        super(message);
    }
}
