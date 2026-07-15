package com.omnitrade.user_service.exception;

public class DuplicateEmailException extends RuntimeException{
    private String email;

    public DuplicateEmailException(String email) {
        super(String.format("Email '%s' is already registered", email));
        this.email = email;
    }

    public String getEmail() { return email; }
}
