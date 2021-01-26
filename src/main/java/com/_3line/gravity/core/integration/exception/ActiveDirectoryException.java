package com._3line.gravity.core.integration.exception;

import com._3line.gravity.core.exceptions.GravityException;

/**
 * @author FortunatusE
 * @date 11/19/2018
 */
public class ActiveDirectoryException extends GravityException {

    public ActiveDirectoryException(String message){
        super(message);
    }

    public ActiveDirectoryException(String message, Throwable cause){
        super(message, cause);
    }
}
