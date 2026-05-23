package com.henrique.taskflow.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String email) {
        super("Email " + email + " ja cadastrado");
    }
}
