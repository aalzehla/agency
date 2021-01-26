package com._3line.gravity.core.verification.exceptions;

public class PendingVerificationException extends VerificationException {

    public PendingVerificationException() {
        super("Failed to perform the requested action");
    }

    public PendingVerificationException(Throwable cause) {
        super("Failed to perform the requested action", cause);
    }

    public PendingVerificationException(String message) {
        super(message);
    }

    public PendingVerificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
