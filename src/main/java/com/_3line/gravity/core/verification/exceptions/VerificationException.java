package com._3line.gravity.core.verification.exceptions;

import com._3line.gravity.core.exceptions.GravityException;

public class VerificationException extends GravityException {
    String message;
    Object obj;

    public VerificationException() {
        super("Failed to perform the requested action");
    }

    public VerificationException(Throwable cause) {
        super("Failed to perform the requested action", cause);
    }

    public VerificationException(String message) {
        this.message = message ;
    }

    public VerificationException(String message, Throwable cause) {
        super(message, cause);
    }
    public VerificationException(String message, Object obj) {
        this.message = message;
        this.obj = obj;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
