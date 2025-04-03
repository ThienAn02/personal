package com.annie.base.exception;

import lombok.Getter;

@Getter
public class IdNotFoundException extends RuntimeException {
    public IdNotFoundException(String message) {
        super(message);
    }
}