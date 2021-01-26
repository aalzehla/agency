package com._3line.gravity.core.usermgt.exception;

import com._3line.gravity.core.exceptions.GravityException;

public class UserNotFoundException extends GravityException {

    public UserNotFoundException() {
        super("User not found");
    }

    public UserNotFoundException(Throwable cause) {
        super("User not found", cause);
    }

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
