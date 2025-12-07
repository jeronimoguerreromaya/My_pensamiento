package com.mypensamiento.mypensamiento.application.exception;

public class NickNameAlreadyExistsException extends RuntimeException {
    public NickNameAlreadyExistsException(String message) {
        super(message);
    }
}
