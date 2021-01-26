package com._3line.gravity.core.files.exceptions;

import com._3line.gravity.core.exceptions.GravityException;

public class FileStorageException extends GravityException {

    String message;
    Object obj;

    public FileStorageException() {
        super("Failed to perform the requested action");
    }

    public FileStorageException(Throwable cause) {
        super("Failed to perform the requested action", cause);
    }

    public FileStorageException(String message) {
        super(message);
    }

    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
    public FileStorageException(String message, Object obj) {
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
