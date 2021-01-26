package com._3line.gravity.core.usermgt.exception;

import com._3line.gravity.core.exceptions.GravityException;

public class AppUserServiceException extends GravityException {
    String message;
    Object obj;

    public AppUserServiceException() {
        super("Failed to perform the requested action");
    }

    public AppUserServiceException(Throwable cause) {
        super("Failed to perform the requested action", cause);
    }

    public AppUserServiceException(String message) {
        this.message = message ;
    }

    public AppUserServiceException(String message, Throwable cause) {
        super(message, cause);
    }
    public AppUserServiceException(String message, Object obj) {
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
