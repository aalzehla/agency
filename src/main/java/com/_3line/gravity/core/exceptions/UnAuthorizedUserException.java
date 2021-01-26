package com._3line.gravity.core.exceptions;

public class UnAuthorizedUserException extends GravityException {


    String message;
    Object obj;

    public UnAuthorizedUserException() {
        super("Not Authorized");
    }

    public UnAuthorizedUserException(Throwable cause) {
        super("Failed to perform the requested action", cause);
    }

    public UnAuthorizedUserException(String message) {
        super(message);
    }

    public UnAuthorizedUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnAuthorizedUserException(String message, Object obj) {
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
