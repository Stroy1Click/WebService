package ru.stroy1click.web.common.exception;

public class ServiceErrorResponseException extends RuntimeException {

    public ServiceErrorResponseException(String message) {
        super(message);
    }
}
