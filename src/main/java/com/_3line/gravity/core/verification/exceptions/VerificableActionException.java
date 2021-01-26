package com._3line.gravity.core.verification.exceptions;

import com._3line.gravity.core.exceptions.GravityException;

public class VerificableActionException extends GravityException {

    public VerificableActionException() {
        super("Failed to perform the requested action");
    }

    public VerificableActionException(Throwable cause) {
        super("Failed to perform the requested action", cause);
    }

    public VerificableActionException(String message) {
        super(message);
    }

    public VerificableActionException(String message, Throwable cause) {
        super(message, cause);
    }
}
