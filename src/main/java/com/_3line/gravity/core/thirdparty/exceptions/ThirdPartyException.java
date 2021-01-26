package com._3line.gravity.core.thirdparty.exceptions;

import com._3line.gravity.core.exceptions.GravityException;

public class ThirdPartyException extends GravityException {
    String message;
    Object obj;

    public ThirdPartyException() {
        super("Failed to perform the requested action");
    }

    public ThirdPartyException(Throwable cause) {
        super("Failed to perform the requested action", cause);
    }

    public ThirdPartyException(String message) {
        this.message = message ;
    }

    public ThirdPartyException(String message, Throwable cause) {
        super(message, cause);
    }
    public ThirdPartyException(String message, Object obj) {
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
