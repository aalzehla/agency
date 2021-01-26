package com._3line.gravity.core.cryptography.exceptions;

import com._3line.gravity.core.exceptions.GravityException;

public class CryptographyException extends GravityException {

    public CryptographyException() {
        super("Failed to perform the requested action");
    }

    public CryptographyException(Throwable cause) {
        super("Failed to perform the requested action", cause);
    }

    public CryptographyException(String message) {
        super(message);
    }

    public CryptographyException(String message, Throwable cause) {
        super(message, cause);
    }
}
