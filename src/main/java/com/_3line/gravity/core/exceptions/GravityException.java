package com._3line.gravity.core.exceptions;

public class GravityException extends RuntimeException {


    String message;
    Object obj;

    public GravityException() {
        super("Failed to perform the requested action");
    }

    public GravityException(Throwable cause) {
        super("Failed to perform the requested action", cause);
    }

    public GravityException(String message) {
        super(message);
    }

    public GravityException(String message, Throwable cause) {
        super(message, cause);
    }

    public GravityException(String message, Object obj) {
        this.message = message;
        this.obj = obj;
    }


    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
