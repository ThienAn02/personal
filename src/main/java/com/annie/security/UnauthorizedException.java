package com.annie.security;

public class UnauthorizedException extends Exception {
    public static final String INVALID_TOKEN = "Invalid token";
    public static final String INVALID_PASSWORD = "Invalid password";

    public UnauthorizedException(String message) {
        super(message);
    }
}
